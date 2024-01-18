package com.shubham.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    @GetMapping("/")
    public String notSecure(){
        return "Hi There, I'm open to everyone";
    }
    @GetMapping("/secure")
    public String secure(Principal principal){
        return "Hello, "+ principal.getName()+" I'm secured endpoint.";
    }
}
