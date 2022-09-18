package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.response.UserSecurityCreateResponse;
import com.ironhack.ironbank.dto.UserSecurityDTO;
import com.ironhack.ironbank.enums.RealmGroup;

import javax.validation.Valid;

public interface SecurityService {

    UserSecurityCreateResponse createUser(@Valid UserSecurityDTO user, RealmGroup group);
    void updateUser(@Valid String id, @Valid UserSecurityDTO user);
    void deleteUser(@Valid String id);
    void sendVerificationLink(@Valid String id);
    void sendResetPassword(@Valid String id);
}
