
# 🏥 Hospital System - Tech Challenge Fase 3

Backend desenvolvido para o Tech Challenge da Fase 3 com foco em:

- 🔐 Segurança com Spring Security
- 📊 Consultas flexíveis com GraphQL
- 🔁 Comunicação assíncrona com RabbitMQ
- 🧩 Arquitetura baseada em microsserviços

---

## 📐 Arquitetura

O projeto segue uma abordagem de microsserviços organizados em um único repositório (monorepo) para facilitar execução e avaliação.

```
hospital-system
 ├── agendamento-service
 ├── notificacao-service
 ├── docker-compose.yml (opcional)
 ├── README.md
 └── LICENSE.md
```
---

## 🔹 Serviços

### 🏥 agendamento-service

Responsável por:

- Cadastro e edição de consultas
- Controle de acesso por perfil (Médico, Enfermeiro, Paciente)
- Exposição de dados via GraphQL
- Publicação de eventos no RabbitMQ

Porta padrão: 8080

---

### 🔔 notificacao-service

Responsável por:

- Consumir eventos do RabbitMQ
- Processar envio de lembretes para pacientes

Serviço **stateless** — não possui banco de dados. A durabilidade das mensagens é garantida pelo RabbitMQ (fila durable). O estado das consultas é de responsabilidade exclusiva do `agendamento-service`.

Porta padrão: 8081

> **Nota sobre o `NotificacaoController`:** O endpoint `GET /notificacoes/paciente/{id}` (porta 8081) é auxiliar e exposto exclusivamente para fins de validação do challenge. Em um ambiente de produção, este serviço operaria exclusivamente como consumer RabbitMQ, sem API REST exposta.

---

## 🔐 Segurança

Implementado com Spring Security utilizando autenticação básica.

Perfis disponíveis:

- ROLE_MEDICO
- ROLE_ENFERMEIRO
- ROLE_PACIENTE

---

## 📊 GraphQL

Permite consultas flexíveis como:

- Listar histórico completo do paciente
- Listar apenas consultas futuras
- Buscar consultas por período

Endpoint padrão:
http://localhost:8080/graphql

---

## 🔁 Comunicação Assíncrona

Utiliza RabbitMQ para comunicação entre serviços.

Fluxo:

1. Consulta criada ou editada
2. Evento publicado na fila
3. Serviço de notificação consome o evento
4. Lembrete é processado

---

## ▶️ Como executar o projeto

1. Subir a infraestrutura (PostgreSQL + RabbitMQ):
```bash
docker compose up -d
```
  - PostgreSQL: `localhost:5432` — database `hospital_system`, user `postgres` / senha `postgres`
  - RabbitMQ: painel em http://localhost:15672 — user `guest` / senha `guest`

2. Executar o `agendamento-service` (porta 8080)
3. Executar o `notificacao-service` (porta 8081)

---

## � API Endpoints

> A coleção completa do Postman está disponível em [`hospital-system.postman_collection.json`](hospital-system.postman_collection.json).  
> Base URL padrão: `http://localhost:8080`

### 🔐 Auth (`/auth`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/auth/login` | Autenticar usuário (HTTP Basic) | Público |
| `POST` | `/auth/register` | Registrar novo paciente | Público |
| `GET` | `/auth/me` | Retorna dados do usuário autenticado | Autenticado |

---

### 👤 Usuários (`/api/users`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/api/users` | Criar usuário | MEDICO, ENFERMEIRO |
| `POST` | `/api/users/bootstrap-admin` | Criar primeiro admin | Público |
| `GET` | `/api/users` | Listar todos os usuários | MEDICO, ENFERMEIRO |
| `GET` | `/api/users/{id}` | Buscar usuário por ID | MEDICO, ENFERMEIRO, PACIENTE (próprio) |
| `GET` | `/api/users/username/{username}` | Buscar pelo username | MEDICO, ENFERMEIRO |
| `GET` | `/api/users/role/{role}` | Listar por perfil | MEDICO, ENFERMEIRO |
| `GET` | `/api/users/active` | Listar usuários ativos | MEDICO, ENFERMEIRO |
| `PUT` | `/api/users/{id}` | Atualizar usuário | MEDICO, ENFERMEIRO, PACIENTE (próprio) |
| `PUT` | `/api/users/{id}/activate` | Ativar usuário | MEDICO, ENFERMEIRO |
| `PUT` | `/api/users/{id}/deactivate` | Desativar usuário | MEDICO, ENFERMEIRO |
| `DELETE` | `/api/users/{id}` | Excluir usuário | MEDICO |

---

### 🩺 Médicos (`/api/medicos`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/api/medicos` | Cadastrar médico | MEDICO, ENFERMEIRO |
| `GET` | `/api/medicos` | Listar todos os médicos | MEDICO, ENFERMEIRO |
| `GET` | `/api/medicos/{id}` | Buscar médico por ID | MEDICO, ENFERMEIRO |
| `GET` | `/api/medicos/crm/{crm}` | Buscar pelo CRM | MEDICO, ENFERMEIRO |
| `GET` | `/api/medicos/user/{userId}` | Buscar pelo ID do usuário | MEDICO, ENFERMEIRO |
| `GET` | `/api/medicos/especialidade/{especialidade}` | Filtrar por especialidade | MEDICO, ENFERMEIRO |
| `GET` | `/api/medicos/active` | Listar médicos ativos | MEDICO, ENFERMEIRO |
| `GET` | `/api/medicos/search?nome=` | Buscar por nome | MEDICO, ENFERMEIRO |
| `PUT` | `/api/medicos/{id}` | Atualizar médico | MEDICO, ENFERMEIRO |
| `PUT` | `/api/medicos/{id}/activate` | Ativar médico | MEDICO, ENFERMEIRO |
| `PUT` | `/api/medicos/{id}/deactivate` | Desativar médico | MEDICO, ENFERMEIRO |
| `DELETE` | `/api/medicos/{id}` | Excluir médico | MEDICO |

Especialidades disponíveis: `CARDIOLOGIA`, `DERMATOLOGIA`, `ORTOPEDIA`, `NEUROLOGIA`, `PEDIATRIA`, `GINECOLOGIA`, `ONCOLOGIA`, `PSIQUIATRIA`, `OFTALMOLOGIA`, `UROLOGIA`, `ENDOCRINOLOGIA`, `GASTROENTEROLOGIA`, `PNEUMOLOGIA`, `REUMATOLOGIA`

---

### 🧑‍⚕️ Enfermeiros (`/api/enfermeiros`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/api/enfermeiros` | Cadastrar enfermeiro | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros` | Listar todos os enfermeiros | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros/{id}` | Buscar enfermeiro por ID | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros/coren/{coren}` | Buscar pelo COREN | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros/user/{userId}` | Buscar pelo ID do usuário | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros/setor/{setor}` | Filtrar por setor | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros/turno/{turno}` | Filtrar por turno | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros/setor/{setor}/turno/{turno}` | Filtrar por setor e turno | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros/especializacao/{especializacao}` | Filtrar por especialização | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros/active` | Listar enfermeiros ativos | MEDICO, ENFERMEIRO |
| `GET` | `/api/enfermeiros/search?nome=` | Buscar por nome | MEDICO, ENFERMEIRO |
| `PUT` | `/api/enfermeiros/{id}` | Atualizar enfermeiro | MEDICO, ENFERMEIRO |
| `PUT` | `/api/enfermeiros/{id}/activate` | Ativar enfermeiro | MEDICO, ENFERMEIRO |
| `PUT` | `/api/enfermeiros/{id}/deactivate` | Desativar enfermeiro | MEDICO, ENFERMEIRO |
| `DELETE` | `/api/enfermeiros/{id}` | Excluir enfermeiro | MEDICO |

---

### 🏥 Pacientes (`/api/pacientes`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/api/pacientes` | Cadastrar paciente | MEDICO, ENFERMEIRO |
| `GET` | `/api/pacientes` | Listar todos os pacientes | MEDICO, ENFERMEIRO |
| `GET` | `/api/pacientes/{id}` | Buscar paciente por ID | MEDICO, ENFERMEIRO, PACIENTE |
| `GET` | `/api/pacientes/cpf/{cpf}` | Buscar pelo CPF | MEDICO, ENFERMEIRO |
| `GET` | `/api/pacientes/user/{userId}` | Buscar pelo ID do usuário | MEDICO, ENFERMEIRO, PACIENTE |
| `GET` | `/api/pacientes/active` | Listar pacientes ativos | MEDICO, ENFERMEIRO |
| `GET` | `/api/pacientes/search?nome=` | Buscar por nome | MEDICO, ENFERMEIRO |
| `PUT` | `/api/pacientes/{id}` | Atualizar paciente | MEDICO, ENFERMEIRO |
| `PUT` | `/api/pacientes/{id}/activate` | Ativar paciente | MEDICO, ENFERMEIRO |
| `PUT` | `/api/pacientes/{id}/deactivate` | Desativar paciente | MEDICO, ENFERMEIRO |
| `DELETE` | `/api/pacientes/{id}` | Excluir paciente | MEDICO |

---

### 📅 Consultas (`/api/consultas`)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/api/consultas` | Agendar consulta | MEDICO, ENFERMEIRO |
| `GET` | `/api/consultas` | Listar todas as consultas | MEDICO, ENFERMEIRO |
| `GET` | `/api/consultas/{id}` | Buscar consulta por ID | MEDICO, ENFERMEIRO, PACIENTE |
| `GET` | `/api/consultas/medico/{medicoId}` | Listar por médico | MEDICO, ENFERMEIRO |
| `GET` | `/api/consultas/paciente/{pacienteId}` | Listar por paciente | MEDICO, ENFERMEIRO, PACIENTE (próprio) |
| `GET` | `/api/consultas/paciente/{pacienteId}/futuras` | Consultas futuras do paciente | MEDICO, ENFERMEIRO, PACIENTE (próprio) |
| `GET` | `/api/consultas/paciente/{pacienteId}/historico` | Histórico completo do paciente | MEDICO, ENFERMEIRO, PACIENTE (próprio) |
| `GET` | `/api/consultas/status/{status}` | Filtrar por status | MEDICO, ENFERMEIRO |
| `GET` | `/api/consultas/periodo?inicio=&fim=` | Filtrar por período (ISO 8601) | MEDICO, ENFERMEIRO |
| `GET` | `/api/consultas/notificacoes` | Consultas para notificação | MEDICO, ENFERMEIRO |
| `PUT` | `/api/consultas/{id}` | Atualizar consulta | MEDICO, ENFERMEIRO |
| `PUT` | `/api/consultas/{id}/status?status=` | Atualizar status | MEDICO, ENFERMEIRO |
| `PUT` | `/api/consultas/{id}/cancelar?motivo=` | Cancelar consulta | MEDICO, ENFERMEIRO |
| `DELETE` | `/api/consultas/{id}` | Excluir consulta | MEDICO, ENFERMEIRO |

Status disponíveis: `AGENDADA`, `CONFIRMADA`, `EM_ANDAMENTO`, `CONCLUIDA`, `CANCELADA`, `REAGENDADA`, `FALTA_PACIENTE`

---

### 📊 GraphQL (`/graphql`)

Endpoint: `POST http://localhost:8080/graphql`  
Interface interativa: `http://localhost:8080/graphiql`

| Operação | Tipo | Descrição | Acesso |
|----------|------|-----------|--------|
| `consultas` | Query | Listar todas as consultas | MEDICO, ENFERMEIRO |
| `consulta(id)` | Query | Buscar consulta por ID | MEDICO, ENFERMEIRO |
| `consultasByMedico(medicoId)` | Query | Listar consultas de um médico | MEDICO, ENFERMEIRO |
| `consultasByPaciente(pacienteId)` | Query | Listar consultas de um paciente | MEDICO, ENFERMEIRO, PACIENTE |
| `consultasFuturasByPaciente(pacienteId)` | Query | Consultas futuras do paciente | MEDICO, ENFERMEIRO, PACIENTE |
| `historicoCompletoPaciente(pacienteId)` | Query | Histórico completo | MEDICO, ENFERMEIRO, PACIENTE |
| `consultasByStatus(status)` | Query | Filtrar por status | MEDICO, ENFERMEIRO |
| `medicos` | Query | Listar todos os médicos | Autenticado |
| `medico(id)` | Query | Buscar médico por ID | Autenticado |
| `medicosByEspecialidade(especialidade)` | Query | Filtrar médicos por especialidade | Autenticado |
| `pacientes` | Query | Listar todos os pacientes | Autenticado |
| `paciente(id)` | Query | Buscar paciente por ID | Autenticado |
| `agendarConsulta(input)` | Mutation | Agendar nova consulta | MEDICO, ENFERMEIRO |
| `atualizarStatusConsulta(id, status)` | Mutation | Mudar status da consulta | MEDICO, ENFERMEIRO |
| `cancelarConsulta(id, motivo)` | Mutation | Cancelar consulta | MEDICO, ENFERMEIRO |

> **Formato do campo `dataHora`:** deve ser uma string no formato ISO-8601 sem timezone: `"2026-04-01T10:00:00"`

---
## 📘 Documentação da API

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **GraphiQL (interface interativa):** http://localhost:8080/graphiql

---
## �🛠 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring GraphQL
- RabbitMQ
- PostgreSQL
- Docker / Docker Compose
- Maven

---

## 👨‍💻 Autores 

Felipe Tiburcio de Araujo

Rodrigo de Alencar Xavier
