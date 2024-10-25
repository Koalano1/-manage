package usermanagement.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usermanagement.model.ERole;
import usermanagement.model.Role;
import usermanagement.model.User;
import usermanagement.repository.RoleRepository;
import usermanagement.repository.UserRepository;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, Admin!");
    }

    @PostMapping
    ResponseEntity<User> createUser() {
        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        User user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("a@gmail.com")
                .roles(new HashSet<>(List.of(adminRole)))
                .build();

        return ResponseEntity.ok(
                userRepository.save(user));
    }

}
