package com.ironhack.ironbank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.model.user.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.keycloak.representations.idm.UserRepresentation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurityDTO extends UserDTO {

    private String username;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    public static UserSecurityDTO fromDTO(AdminDTO adminDTO) {
        var userSecurityDTO = new UserSecurityDTO();
        if(adminDTO.getId() != null) {
            userSecurityDTO.setId(adminDTO.getId());
        }
        userSecurityDTO.setFirstName(adminDTO.getFirstName());
        userSecurityDTO.setLastName(adminDTO.getLastName());
        userSecurityDTO.setUsername(adminDTO.getUsername());
        userSecurityDTO.setEmail(adminDTO.getEmail());
        userSecurityDTO.setPassword(adminDTO.getPassword());

        return userSecurityDTO;
    }

    public static UserSecurityDTO fromDTO(AccountHolderDTO accountHolderDTO) {
        var userSecurityDTO = new UserSecurityDTO();
        if (accountHolderDTO.getId() != null) {
            userSecurityDTO.setId(accountHolderDTO.getId());
        }
        userSecurityDTO.setFirstName(accountHolderDTO.getFirstName());
        userSecurityDTO.setLastName(accountHolderDTO.getLastName());
        userSecurityDTO.setUsername(accountHolderDTO.getUsername());
        userSecurityDTO.setEmail(accountHolderDTO.getEmail());
        userSecurityDTO.setPassword(accountHolderDTO.getPassword());

        return userSecurityDTO;
    }

    public static UserSecurityDTO fromEntity(Admin admin, String password) {
        var userSecurityDTO = new UserSecurityDTO();
        if (admin.getId() != null) {
            userSecurityDTO.setId(admin.getId());
        }
        userSecurityDTO.setFirstName(admin.getFirstName());
        userSecurityDTO.setLastName(admin.getLastName());
        userSecurityDTO.setUsername(admin.getUsername());
        userSecurityDTO.setEmail(admin.getEmail());
        userSecurityDTO.setPassword(password);

        return userSecurityDTO;
    }

    public static UserSecurityDTO fromEntity(AccountHolder accountHolder, String password) {
        var userSecurityDTO = new UserSecurityDTO();
        if (accountHolder.getId() != null) {
            userSecurityDTO.setId(accountHolder.getId());
        }
        userSecurityDTO.setFirstName(accountHolder.getFirstName());
        userSecurityDTO.setLastName(accountHolder.getLastName());
        userSecurityDTO.setUsername(accountHolder.getUsername());
        userSecurityDTO.setEmail(accountHolder.getEmail());
        userSecurityDTO.setPassword(password);

        return userSecurityDTO;
    }

    public static UserSecurityDTO fromUserRepresentation(UserRepresentation userRepresentation) {
        var userSecurityDTO = new UserSecurityDTO();
        userSecurityDTO.setId(userRepresentation.getId());
        userSecurityDTO.setFirstName(userRepresentation.getFirstName());
        userSecurityDTO.setLastName(userRepresentation.getLastName());
        userSecurityDTO.setUsername(userRepresentation.getUsername());
        userSecurityDTO.setEmail(userRepresentation.getEmail());
        return userSecurityDTO;
    }
}
