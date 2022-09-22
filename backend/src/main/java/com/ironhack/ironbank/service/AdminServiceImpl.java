package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.response.AdminCreateResponse;
import com.ironhack.ironbank.dto.AdminDTO;
import com.ironhack.ironbank.dto.UserSecurityDTO;
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
    public AdminCreateResponse create(AdminDTO adminDTO) {
        var response = new AdminCreateResponse();
        if (adminDTO.getId() != null && adminRepository.findById(adminDTO.getId()).isPresent()) {
            response.setStatus(409);
            response.setMessage("User already exists");
            return response;
        }

        var userSecurityDTO = UserSecurityDTO.fromDTO(adminDTO);
        var serviceResponse = securityService.createUser(userSecurityDTO, RealmGroup.ADMINS);
        response.setStatus(serviceResponse.getStatus());
        UserSecurityDTO userSecurityDTOCreated = serviceResponse.getUser();

        if (response.getStatus() == 201) {
            var admin = Admin.fromDTO(adminDTO);
            admin.setId(userSecurityDTOCreated.getId());
            admin = adminRepository.save(admin);
            response.setAdmin(AdminDTO.fromEntity(admin));
        } else if (response.getStatus() == 409) {
            var admin = adminRepository.findById(userSecurityDTOCreated.getId()).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
            response.setAdmin(AdminDTO.fromEntity(admin));
        }

        return response;
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

        var userSecurityDTO = UserSecurityDTO.fromDTO(adminDTO);
        securityService.updateUser(admin.getId(), userSecurityDTO);

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
