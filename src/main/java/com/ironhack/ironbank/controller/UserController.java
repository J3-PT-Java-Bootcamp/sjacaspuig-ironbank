package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.ResponseMessage;
import com.ironhack.ironbank.enums.RealmGroup;
import com.ironhack.ironbank.model.UserKeycloak;
import com.ironhack.ironbank.service.SecurityServiceImpl;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private SecurityServiceImpl securityServiceImpl;


    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> create(@RequestBody UserKeycloak user){

        Faker faker = new Faker();
        // Set group as admin or user depending the random number
        RealmGroup group = faker.random().nextInt(0, 2) == 0 ? RealmGroup.ADMINS : RealmGroup.USERS;

        Object[] obj = securityServiceImpl.createUser(user, group);
        int status = (int) obj[0];
        ResponseMessage message = (ResponseMessage) obj[1];
        return ResponseEntity.status(status).body(message);
    }
}
