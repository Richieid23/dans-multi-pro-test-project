package com.danspro.testproject.controllers;

import com.danspro.testproject.dto.Response;
import com.danspro.testproject.entities.User;
import com.danspro.testproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody User user, Errors errors){
        Response response = new Response();

        if (errors.hasErrors()) {
            Map<String, Object> responseData = new HashMap<>();
            List<String> errorMessage = new ArrayList<>();
            for (ObjectError error: errors.getAllErrors()) {
                errorMessage.add(error.getDefaultMessage());
            }

            responseData.put("errorMessage", errorMessage);
            response = Response.builder()
                    .rc("403")
                    .message("error")
                    .data(responseData)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response = userService.register(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> register(HttpServletRequest httpRequest, @Valid @RequestBody User user, Errors errors){
        Response response = new Response();

        if (errors.hasErrors()) {
            Map<String, Object> responseData = new HashMap<>();
            List<String> errorMessage = new ArrayList<>();
            for (ObjectError error: errors.getAllErrors()) {
                errorMessage.add(error.getDefaultMessage());
            }

            responseData.put("errorMessage", errorMessage);
            response = Response.builder()
                    .rc("403")
                    .message("error")
                    .data(responseData)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response = userService.login(httpRequest, user);
        return ResponseEntity.ok(response);
    }
}
