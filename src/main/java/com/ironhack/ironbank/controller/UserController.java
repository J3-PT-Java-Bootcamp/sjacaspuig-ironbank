package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.ResponseMessage;
import com.ironhack.ironbank.model.UserKeycloak;
import com.ironhack.ironbank.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private KeycloakService keycloakService;


    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> create(@RequestBody UserKeycloak user){
        Object[] obj = keycloakService.createUser(user);
        int status = (int) obj[0];
        ResponseMessage message = (ResponseMessage) obj[1];
        return ResponseEntity.status(status).body(message);
    }
}
