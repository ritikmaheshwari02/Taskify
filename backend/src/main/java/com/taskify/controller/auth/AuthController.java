package com.taskify.controller.auth;

import com.taskify.dto.SignUpRequest;
import com.taskify.dto.UserDto;
import com.taskify.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        if (authService.hasUserWithEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                                 .body("User already exist with this email");
        }
        UserDto userDto = authService.signUpUser(signUpRequest);
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("User not created");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(userDto);
    }
}
