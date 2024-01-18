package com.shubham.controller;

import com.shubham.models.JwtRequest;
import com.shubham.models.JwtResponse;
import com.shubham.security.JwtAuthHelper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager manager;

    @Autowired
    JwtAuthHelper authHelper;
    @Autowired
    UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        authenticate(request.getUserId(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserId());
        String token = authHelper.generateToken(userDetails.getUsername());

        JwtResponse response = JwtResponse.builder()
                .token(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String userId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(userId, password);
        try{
            manager.authenticate(authenticationToken);
        }catch (BadCredentialsException exception){
            throw new BadCredentialsException("Invalid username and password " + exception);
        }
    }
}
