package com.SpringBoot.Bus_Reservation_System.controller;

import com.SpringBoot.Bus_Reservation_System.model.User;
import com.SpringBoot.Bus_Reservation_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allow React frontend (any origin)
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // BCrypt automatically salts + hashes the password
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ── REGISTER ────────────────────────────────────────────────────────────
    // Called by: Register.jsx → POST /api/auth/register
    // Body:      { "name": "Vasanth", "email": "v@gmail.com", "password": "abc123" }
    // Saves:     User with hashed password to MongoDB "users" collection
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String name     = body.get("name");
        String email    = body.get("email");
        String password = body.get("password");

        // Reject if email already registered
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use.");
        }

        // Build the User and hash the password before saving
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encoder.encode(password)); // e.g. "$2a$10$xyz..."

        userRepository.save(user); // ← persists to MongoDB

        // Return the safe user info (no password!)
        return ResponseEntity.ok(Map.of("name", user.getName(), "email", user.getEmail()));
    }

    // ── LOGIN ────────────────────────────────────────────────────────────────
    // Called by: Login.jsx → POST /api/auth/login
    // Body:      { "email": "v@gmail.com", "password": "abc123" }
    // Verifies:  BCrypt.matches(inputPassword, storedHash)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");

        // Look up user by email in MongoDB
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // Don't reveal whether it's email or password that's wrong (security)
            return ResponseEntity.status(401).body("Invalid email or password.");
        }

        // Compare what the user typed against the stored BCrypt hash
        boolean passwordMatches = encoder.matches(password, user.getPassword());

        if (!passwordMatches) {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }

        // Success — return safe user info
        return ResponseEntity.ok(Map.of("name", user.getName(), "email", user.getEmail()));
    }
}
