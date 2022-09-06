package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AdminDTO;
import com.ironhack.ironbank.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        return null;
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
