package com.nivash.expense_tracker.service;

import com.nivash.expense_tracker.dto.LoginRequest;
import com.nivash.expense_tracker.dto.SignupRequest;
import com.nivash.expense_tracker.entity.User;
import com.nivash.expense_tracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nivash.expense_tracker.dto.LoginRequest;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }
    
    public User login(LoginRequest request) {
    	User user=userRepository.findByEmail(request.getEmail())
    			.orElseThrow(() -> new RuntimeException("Invalid Email or Password"));
    	if(!passwordEncoder.matches(request.getPassword(),user.getPassword())) {
    		throw new RuntimeException("Invalid email or password");
    	}
    	return user;
    }
}