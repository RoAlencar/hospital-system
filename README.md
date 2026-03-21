
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

Porta padrão: 8081

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

## 🛠 Tecnologias Utilizadas

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
