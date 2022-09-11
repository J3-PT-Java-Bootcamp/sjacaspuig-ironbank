package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.user.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AdminDTO extends UserSecurityDTO {

    public static AdminDTO fromEntity(Admin admin) {
        var adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setFirstName(admin.getFirstName());
        adminDTO.setLastName(admin.getLastName());
        adminDTO.setUsername(admin.getUsername());
        adminDTO.setEmail(admin.getEmail());
        return adminDTO;
    }

    public static List<AdminDTO> fromEntities(List<Admin> admins) {
        return admins.stream().map(AdminDTO::fromEntity).toList();
    }
}
