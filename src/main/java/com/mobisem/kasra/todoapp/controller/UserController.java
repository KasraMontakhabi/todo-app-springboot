package com.mobisem.kasra.todoapp.controller;

import com.mobisem.kasra.todoapp.model.Users;
import com.mobisem.kasra.todoapp.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.mobisem.kasra.todoapp.security.SecurityConstants.EXPIRATION_TIME;
import static com.mobisem.kasra.todoapp.security.SecurityConstants.SECRET;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

@PostMapping("/register")
public ResponseEntity<Map<String, String>> registerUser(@RequestBody Users user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    Users savedUser = userRepository.save(user);
    Map<String, String> response = new HashMap<>();
    response.put("message", "user created");
    response.put("userId", savedUser.getId());
    return ResponseEntity.ok(response);
}

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody Users users) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword());
        Authentication authenticated = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        String token = Jwts.builder()
                .setSubject(users.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "logged in successfully");
        response.put("token", "Bearer " + token);

        return ResponseEntity.ok().body(response);

    }
}
