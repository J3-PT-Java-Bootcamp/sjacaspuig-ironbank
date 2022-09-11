package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.enums.RealmGroup;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.model.user.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserKeycloakDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private List<String> groups;

    public static UserKeycloakDTO fromDTO(AdminDTO adminDTO, String password) {
        var userKeycloakDTO = new UserKeycloakDTO();
        if(adminDTO.getId() != null) {
            userKeycloakDTO.setId(adminDTO.getId());
        }
        userKeycloakDTO.setFirstName(adminDTO.getFirstName());
        userKeycloakDTO.setLastName(adminDTO.getLastName());
        userKeycloakDTO.setUsername(adminDTO.getUsername());
        userKeycloakDTO.setEmail(adminDTO.getEmail());
        userKeycloakDTO.setPassword(password);
        userKeycloakDTO.setGroups(List.of(RealmGroup.ADMINS.toString()));

        return userKeycloakDTO;
    }

    public static UserKeycloakDTO fromDTO(AccountHolderDTO accountHolderDTO, String password) {
        var userKeycloakDTO = new UserKeycloakDTO();
        if (accountHolderDTO.getId() != null) {
            userKeycloakDTO.setId(accountHolderDTO.getId());
        }
        userKeycloakDTO.setFirstName(accountHolderDTO.getFirstName());
        userKeycloakDTO.setLastName(accountHolderDTO.getLastName());
        userKeycloakDTO.setUsername(accountHolderDTO.getUsername());
        userKeycloakDTO.setEmail(accountHolderDTO.getEmail());
        userKeycloakDTO.setPassword(password);
        userKeycloakDTO.setGroups(List.of(RealmGroup.USERS.toString()));

        return userKeycloakDTO;
    }

    public static UserKeycloakDTO fromEntity(Admin admin, String password) {
        var userKeycloakDTO = new UserKeycloakDTO();
        if (admin.getId() != null) {
            userKeycloakDTO.setId(admin.getId());
        }
        userKeycloakDTO.setFirstName(admin.getFirstName());
        userKeycloakDTO.setLastName(admin.getLastName());
        userKeycloakDTO.setUsername(admin.getUsername());
        userKeycloakDTO.setEmail(admin.getEmail());
        userKeycloakDTO.setPassword(password);
        userKeycloakDTO.setGroups(List.of(RealmGroup.ADMINS.toString()));

        return userKeycloakDTO;
    }

    public static UserKeycloakDTO fromEntity(AccountHolder accountHolder, String password) {
        var userKeycloakDTO = new UserKeycloakDTO();
        if (accountHolder.getId() != null) {
            userKeycloakDTO.setId(accountHolder.getId());
        }
        userKeycloakDTO.setFirstName(accountHolder.getFirstName());
        userKeycloakDTO.setLastName(accountHolder.getLastName());
        userKeycloakDTO.setUsername(accountHolder.getUsername());
        userKeycloakDTO.setEmail(accountHolder.getEmail());
        userKeycloakDTO.setPassword(password);
        userKeycloakDTO.setGroups(List.of(RealmGroup.USERS.toString()));

        return userKeycloakDTO;
    }

    public static UserKeycloakDTO fromUserRepresentation(UserRepresentation userRepresentation) {
        var userKeycloakDTO = new UserKeycloakDTO();
        userKeycloakDTO.setId(userRepresentation.getId());
        userKeycloakDTO.setFirstName(userRepresentation.getFirstName());
        userKeycloakDTO.setLastName(userRepresentation.getLastName());
        userKeycloakDTO.setUsername(userRepresentation.getUsername());
        userKeycloakDTO.setEmail(userRepresentation.getEmail());
        userKeycloakDTO.setGroups(userRepresentation.getGroups());
        return userKeycloakDTO;
    }

}
