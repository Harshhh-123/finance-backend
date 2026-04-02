package finance_backend.service;

import finance_backend.model.User;
import finance_backend.repository.UserRepository;
import finance_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User createUser(String username, String email, String password, User.Role role) {
        if (userRepository.existsByUsername(username))
            throw new RuntimeException("Username already exists");
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new RuntimeException("Invalid password");
        if (!user.isActive())
            throw new RuntimeException("User is inactive");
        return jwtUtil.generateToken(username, user.getRole().name());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateRole(Long id, User.Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return userRepository.save(user);
    }

    public User updateStatus(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(active);
        return userRepository.save(user);
    }
}