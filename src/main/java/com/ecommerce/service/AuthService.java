package com.ecommerce.service;

import com.ecommerce.dto.request.LoginRequestDto;
import com.ecommerce.dto.request.RegisterRequestDto;
import com.ecommerce.dto.response.AuthResponseDto;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto requestDto);
    AuthResponseDto login(LoginRequestDto requestDto);
}
