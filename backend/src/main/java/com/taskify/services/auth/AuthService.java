package com.taskify.services.auth;

import com.taskify.dto.SignUpRequest;
import com.taskify.dto.UserDto;

public interface AuthService {
    UserDto signUpUser(SignUpRequest signUpRequest);
    
    boolean hasUserWithEmail(String email);
}
