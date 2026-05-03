package com.taskify.services.auth;

import com.taskify.entities.User;
import com.taskify.enums.UserRole;
import com.taskify.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;

    public void createAnAdminAccount() {
        Optional<User> optionalUser = userRepo.findByUserRole(UserRole.ADMIN);
        if (optionalUser.isEmpty()) {
//            User user = new User();
//            user.setEmail("admin@test.com");
//            user.setName("admin");
//            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
//            user.setUserRole(UserRole.ADMIN);
//            System.out.println("Admin account created successfully");
            User user = User.builder()
                            .email("admin@test.com")
                            .name("admin")
                            .password(new BCryptPasswordEncoder().encode("admin"))
                            .userRole(UserRole.ADMIN)
                            .build();
            userRepo.save(user);
            System.out.println("Admin account created successfully");
        } else {
            System.out.println("Admin account already exist");
        }
    }
}
