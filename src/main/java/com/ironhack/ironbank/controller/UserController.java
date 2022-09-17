package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.KeycloakUserRespone;
import com.ironhack.ironbank.dto.UserSecurityDTO;
import com.ironhack.ironbank.enums.RealmGroup;
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
    public ResponseEntity<UserSecurityDTO> create(@RequestBody UserSecurityDTO user){

        Faker faker = new Faker();
        // Set group as admin or user depending the random number
        RealmGroup group = faker.random().nextInt(0, 2) == 0 ? RealmGroup.ADMINS : RealmGroup.USERS;

        KeycloakUserRespone response = securityServiceImpl.createUser(user, group);
        int status = response.getStatus();
        UserSecurityDTO userSecurityDTO = response.getUser();
        return ResponseEntity.status(status).body(userSecurityDTO);
    }
}
