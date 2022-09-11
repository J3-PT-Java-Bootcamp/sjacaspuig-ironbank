package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserKeycloakController {

    private final SecurityService keycloakService;

    @GetMapping(path = "/verification-link/{id}")
    public String sendVerificationLink(@PathVariable String id){
        keycloakService.sendVerificationLink(id);
        return "Verification Link Send to Registered E-mail Id.";
    }

    @GetMapping(path = "/reset-password/{id}")
    public String sendResetPassword(@PathVariable String id){
        keycloakService.sendResetPassword(id);
        return "Reset Password Link Send Successfully to Registered E-mail Id.";
    }
}
