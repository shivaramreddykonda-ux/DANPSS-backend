
package com.danpss.controller;

import com.danpss.entity.User;
import com.danpss.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getFullName() == null ||
                user.getEmail() == null ||
                user.getPassword() == null ||
                user.getRole() == null ||
                user.getFullName().isBlank() ||
                user.getEmail().isBlank() ||
                user.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            "Enter all required details and a password of at least 8 characters.")
            );
        }

        String email = user.getEmail().trim().toLowerCase();
        String role = user.getRole().trim().toUpperCase();

        if (!role.equals("STUDENT") && !role.equals("ALUMNI")) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Role must be STUDENT or ALUMNI.")
            );
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("message", "Email is already registered.")
            );
        }

        user.setFullName(user.getFullName().trim());
        user.setEmail(email);
        user.setRole(role);
        user.setPassword(encoder.encode(user.getPassword()));

        User saved = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Registration successful!",
                        "id", saved.getId(),
                        "fullName", saved.getFullName(),
                        "email", saved.getEmail(),
                        "role", saved.getRole()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        String password = data.get("password");

        if (email == null || password == null ||
                email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Email and password are required.")
            );
        }

        User user = userRepository
                .findByEmail(email.trim().toLowerCase())
                .orElse(null);

        if (user == null ||
                !encoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Invalid email or password.")
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "message", "Login successful!",
                        "id", user.getId(),
                        "fullName", user.getFullName(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                )
        );
    }
}
