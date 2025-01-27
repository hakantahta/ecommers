package com.ecommerce.controller;

import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.RegisterRequestDto;
import com.ecommerce.dto.response.AuthResponseDto;
import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account and returns JWT token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully registered"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation error"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.register(registerRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }
} 
