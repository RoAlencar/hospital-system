package br.com.fiap.app.agendamentoService.service;

import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Role;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.exception.ResourceNotFoundException;
import br.com.fiap.app.agendamentoService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username já existe: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já existe: " + request.getEmail());
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setActive(true);
        return userRepository.save(request);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", id));
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "username", username));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    public User updateUser(Long id, User request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", id));

        if (request.getNome() != null) {
            user.setNome(request.getNome());
        }
        if (request.getEmail() != null) {
            if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException("Email já existe: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getTelefone() != null) {
            user.setTelefone(request.getTelefone());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", id));
        userRepository.delete(user);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isDatabaseEmpty() {
        return userRepository.count() == 0;
    }

    public User createBootstrapAdmin(User request) {
        // Only allow bootstrap creation when database is empty
        if (!isDatabaseEmpty()) {
            throw new BusinessException("Bootstrap admin creation is only allowed when no users exist");
        }
        
        // Force role to be MEDICO for bootstrap admin
        request.setRole(Role.ROLE_MEDICO);
        
        return createUser(request);
    }

    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", id));
        user.setActive(true);
        userRepository.save(user);
    }
}
