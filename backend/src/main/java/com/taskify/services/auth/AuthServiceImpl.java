package com.taskify.services.auth;

import com.taskify.dto.SignUpRequest;
import com.taskify.dto.UserDto;
import com.taskify.entities.User;
import com.taskify.enums.UserRole;
import com.taskify.repository.UserRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;

    @PostConstruct
    public void createAnAdminAccount() {
        Optional<User> optionalUser = userRepo.findByUserRole(UserRole.ADMIN);
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setPassword(new BCryptPasswordEncoder(12).encode("admin"));
            user.setUserRole(UserRole.ADMIN);
            userRepo.save(user);
            System.out.println("Admin account created successfully");
        } else {
            System.out.println("Admin account already exist");
        }
    }

    @Override
    public UserDto signUpUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder(12).encode(signUpRequest.getPassword()));
        user.setUserRole(UserRole.EMPLOYEE);

        User createdUser = userRepo.save(user);

        return createdUser.getUserDto();
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepo.findFirstByEmail(email)
                       .isPresent();
    }
}
