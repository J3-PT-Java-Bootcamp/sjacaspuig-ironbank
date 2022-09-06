package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.user.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminDTO extends UserDTO {

    public static AdminDTO fromEntity(Admin admin) {
        var adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setName(admin.getName());
        return adminDTO;
    }
}
