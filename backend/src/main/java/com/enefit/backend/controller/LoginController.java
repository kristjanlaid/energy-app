package com.enefit.backend.controller;

import com.enefit.backend.dto.LoginRequest;
import com.enefit.backend.entity.Customer;
import com.enefit.backend.security.JwtUtil;
import com.enefit.backend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * REST controller for handling authentication.
 */
@RestController
public class LoginController {

    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginController(CustomerService customerService,
                           BCryptPasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /login endpoint to authenticate users and return a JWT token if credentials are valid.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Customer> optionalCustomer = customerService.findByUsername(request.getUsername());

        if (optionalCustomer.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        Customer customer = optionalCustomer.get();
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), customer.getPassword());

        if (!passwordMatches) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        // Generate JWT token on successful login
        String token = jwtUtil.generateToken(customer.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
