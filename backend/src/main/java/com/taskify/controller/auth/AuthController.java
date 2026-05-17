package com.taskify.controller.auth;

import com.taskify.dto.AuthenticationRequest;
import com.taskify.dto.AuthenticationResponse;
import com.taskify.dto.SignUpRequest;
import com.taskify.dto.UserDto;
import com.taskify.entities.User;
import com.taskify.repository.UserRepo;
import com.taskify.services.auth.AuthService;
import com.taskify.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    //    private final UserService userService;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

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

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
//        final UserDetails userDetails = userService.userDetailsService()
//                                                   .loadUserByUsername(authenticationRequest.getEmail());
        User user = userRepo.findFirstByEmail(authenticationRequest.getEmail())
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        final String jwt = jwtUtil.generateToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationResponse.setJwt(jwt);
        authenticationResponse.setUserId(user.getId());
        authenticationResponse.setUserRole(user.getUserRole());

        return authenticationResponse;
    }
}
