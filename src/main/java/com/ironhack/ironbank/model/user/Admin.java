package com.ironhack.ironbank.model.user;

import com.ironhack.ironbank.dto.AdminDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "admins")
public class Admin extends User {

    public static Admin fromDTO(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setId(adminDTO.getId());
        admin.setName(adminDTO.getName());
        return admin;
    }
}
