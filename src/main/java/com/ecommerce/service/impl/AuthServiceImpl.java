package com.ecommerce.service.impl;

import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.RegisterRequestDto;
import com.ecommerce.dto.response.AuthResponseDto;
import com.ecommerce.model.User;
import com.ecommerce.model.UserRole;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto requestDto) {
        try {
            if (userRepository.existsByEmail(requestDto.getEmail())) {
                throw new RuntimeException("Email already exists");
            }

            validateRegistrationRequest(requestDto);

            User user = new User();
            user.setEmail(requestDto.getEmail());
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            user.setName(requestDto.getName());
            user.setRole(UserRole.CUSTOMER);
            
            User savedUser = userRepository.save(user);
            String token = jwtService.generateToken(savedUser);
            
            return new AuthResponseDto(token);
        } catch (Exception e) {
            throw new RuntimeException("Error during registration: " + e.getMessage());
        }
    }

    private void validateRegistrationRequest(RegisterRequestDto requestDto) {
        if (requestDto.getEmail() == null || requestDto.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email cannot be empty");
        }
        if (requestDto.getPassword() == null || requestDto.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }
        if (requestDto.getName() == null || requestDto.getName().trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }
    }

    @Override
    public AuthResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponseDto(token);
    }
}
