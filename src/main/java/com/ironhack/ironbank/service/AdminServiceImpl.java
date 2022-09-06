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
        // TODO: Implement this method
        var admin = Admin.fromDTO(adminDTO);
        admin = adminRepository.save(admin);
        return AdminDTO.fromEntity(admin);
    }

    @Override
    public AdminDTO findById(Long id) {
        return null;
    }

    @Override
    public List<AdminDTO> findAll() {
        return null;
    }

    @Override
    public AdminDTO update(Long id, AdminDTO adminDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
