package com.nivash.expense_tracker.controller;

import com.nivash.expense_tracker.dto.LoginRequest;
import com.nivash.expense_tracker.dto.SignupRequest;
import com.nivash.expense_tracker.entity.User;
import com.nivash.expense_tracker.security.JwtUtil;
import com.nivash.expense_tracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        User savedUser = userService.signup(request);
        return ResponseEntity.ok("User registered with id: " + savedUser.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.login(request);
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok("Bearer " + token);
    }
}