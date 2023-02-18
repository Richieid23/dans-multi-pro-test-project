package com.danspro.testproject.services;

import com.danspro.testproject.dto.Response;
import com.danspro.testproject.entities.User;
import com.danspro.testproject.helper.PasswordEncrypter;
import com.danspro.testproject.repositories.UserRepos;
import com.danspro.testproject.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepos userRepos;

    @Autowired
    private JwtService jwtService;

    public Response register(User user) {
        Boolean isUsernameExist = userRepos.existsByUsername(user.getUsername());
        if (isUsernameExist) {
            return Response.builder()
                    .rc("403")
                    .message("Username already exist")
                    .build();
        }


        String encodedPass = PasswordEncrypter.encrypt(user.getPassword());
        user.setPassword(encodedPass);
        userRepos.save(user);

        return Response.builder()
                .rc("000")
                .message("Register successfully")
                .build();
    }

    public Response login(HttpServletRequest httpRequest, User user) {
        Optional<User> checkUser = userRepos.findByUsername(user.getUsername());

        if (checkUser.isEmpty()) {
            return Response.builder()
                    .rc("999")
                    .message("User not found")
                    .build();
        }

        User userData = checkUser.get();
        String encryptedPass = PasswordEncrypter.encrypt(user.getPassword());
        if (!userData.getPassword().trim().equals(encryptedPass)){
            return Response.builder()
                    .rc("999")
                    .message("Password incorrect")
                    .build();
        }

        String token = jwtService.generate(httpRequest, userData);

        return Response.builder()
                .rc("000")
                .message("Login success")
                .data(token)
                .build();
    }
}
