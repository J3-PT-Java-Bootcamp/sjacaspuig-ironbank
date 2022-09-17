package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.UserKeycloakDTO;
import com.ironhack.ironbank.enums.RealmGroup;
import com.ironhack.ironbank.model.UserKeycloak;

import javax.validation.Valid;

public interface SecurityService {

    Object[] createUser(@Valid UserKeycloak user, RealmGroup group);
    void updateUser(@Valid String id, @Valid UserKeycloakDTO userKeycloakDTO);
    void deleteUser(@Valid String id);
    void sendVerificationLink(@Valid String id);
    void sendResetPassword(@Valid String id);
}
