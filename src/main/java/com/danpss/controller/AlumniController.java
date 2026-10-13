
package com.danpss.controller;

import com.danpss.entity.User;
import com.danpss.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumni")
@CrossOrigin(origins = "http://localhost:5173")
public class AlumniController {

    private final UserRepository userRepository;

    public AlumniController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Map<String, Object>> getAlumni(
            @RequestParam(required = false) String search) {

        return userRepository.findAll().stream()
                .filter(user -> "ALUMNI".equalsIgnoreCase(user.getRole()))
                .filter(user -> {
                    if (search == null || search.isBlank()) {
                        return true;
                    }

                    String term = search.trim().toLowerCase();

                    return user.getFullName().toLowerCase().contains(term)
                            || user.getEmail().toLowerCase().contains(term);
                })
                .map(user -> Map.<String, Object>of(
                        "id", user.getId(),
                        "fullName", user.getFullName(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                ))
                .toList();
    }
}
