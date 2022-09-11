package com.ironhack.ironbank.model.user;

import com.ironhack.ironbank.dto.AdminDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "admins")
public class Admin extends UserSecurity {

    public static Admin fromDTO(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setId(adminDTO.getId());
        admin.setFirstName(adminDTO.getFirstName());
        admin.setLastName(adminDTO.getLastName());
        admin.setUsername(adminDTO.getUsername());
        admin.setEmail(adminDTO.getEmail());
        return admin;
    }
}
