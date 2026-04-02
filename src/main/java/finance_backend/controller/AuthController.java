package finance_backend.controller;

import finance_backend.model.User;
import finance_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            User.Role role = body.containsKey("role")
                    ? User.Role.valueOf(body.get("role").toUpperCase())
                    : User.Role.VIEWER;
            User user = userService.createUser(
                    body.get("username"),
                    body.get("email"),
                    body.get("password"),
                    role
            );
            return ResponseEntity.ok(Map.of(
                    "message", "User created successfully",
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "role", user.getRole()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String token = userService.login(body.get("username"), body.get("password"));
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}