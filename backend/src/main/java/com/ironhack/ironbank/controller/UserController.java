package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.response.UserSecurityCreateResponse;
import com.ironhack.ironbank.dto.UserSecurityDTO;
import com.ironhack.ironbank.enums.RealmGroup;
import com.ironhack.ironbank.service.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private SecurityServiceImpl securityServiceImpl;


    @PostMapping("/verification-link")
    public ResponseEntity<Void> sendVerificationLink(@RequestParam String id) {
        securityServiceImpl.sendVerificationLink(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> sendResetPassword(@RequestParam String id) {
        securityServiceImpl.sendResetPassword(id);
        return ResponseEntity.noContent().build();
    }
}