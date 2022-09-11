package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AdminDTO;
import com.ironhack.ironbank.dto.UserKeycloakDTO;
import com.ironhack.ironbank.enums.RealmGroup;
import com.ironhack.ironbank.model.user.Admin;
import com.ironhack.ironbank.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final SecurityService securityService;

    @Override
    public AdminDTO create(AdminDTO adminDTO, String password) {
        if (adminDTO.getId() != null && adminRepository.findById(adminDTO.getId()).isPresent()) {
            throw new IllegalArgumentException("Admin already exists");
        }

        var userKeycloakDTO = UserKeycloakDTO.fromDTO(adminDTO, password);
        var serviceResponse = securityService.createUser(userKeycloakDTO, RealmGroup.ADMINS);
        var status = (Integer) serviceResponse[0];
        UserKeycloakDTO userKeycloakDTOCreated = (UserKeycloakDTO) serviceResponse[1];

        if (status == 201) {
            var admin = Admin.fromDTO(adminDTO);
            admin.setId(userKeycloakDTOCreated.getId());
            admin = adminRepository.save(admin);
            return AdminDTO.fromEntity(admin);
        } else if (status == 409) {
            var admin = adminRepository.findById(userKeycloakDTOCreated.getId()).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
            return AdminDTO.fromEntity(admin);
        } else {
            throw new IllegalArgumentException("Status: " + status);
        }
    }

    @Override
    public AdminDTO findById(String id) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        return AdminDTO.fromEntity(admin);
    }

    @Override
    public List<AdminDTO> findAll() {
        var admins = adminRepository.findAll();
        return AdminDTO.fromEntities(admins);
    }

    @Override
    public AdminDTO update(String id, AdminDTO adminDTO) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        var userKeycloakDTO = UserKeycloakDTO.fromDTO(adminDTO, null);
        securityService.updateUser(admin.getId(), userKeycloakDTO);

        var adminUpdated = Admin.fromDTO(adminDTO);
        adminUpdated.setId(admin.getId());
        adminUpdated = adminRepository.save(adminUpdated);
        return AdminDTO.fromEntity(adminUpdated);
    }

    @Override
    public void delete(String id) {
        securityService.deleteUser(id);
        adminRepository.deleteById(id);
    }
}
