package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AdminDTO;
import com.ironhack.ironbank.model.user.Admin;
import com.ironhack.ironbank.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public AdminDTO create(AdminDTO adminDTO) {
        if (adminDTO.getId() != null && adminRepository.findById(adminDTO.getId()).isPresent()) {
            throw new IllegalArgumentException("Admin already exists");
        }

        var admin = Admin.fromDTO(adminDTO);
        admin = adminRepository.save(admin);
        return AdminDTO.fromEntity(admin);
    }

    @Override
    public AdminDTO findById(Long id) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        return AdminDTO.fromEntity(admin);
    }

    @Override
    public List<AdminDTO> findAll() {
        var admins = adminRepository.findAll();
        return AdminDTO.fromEntities(admins);
    }

    @Override
    public AdminDTO update(Long id, AdminDTO adminDTO) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        var adminUpdated = Admin.fromDTO(adminDTO);
        adminUpdated.setId(admin.getId());
        adminUpdated = adminRepository.save(adminUpdated);
        return AdminDTO.fromEntity(adminUpdated);
    }

    @Override
    public void delete(Long id) {
        adminRepository.deleteById(id);
    }
}
