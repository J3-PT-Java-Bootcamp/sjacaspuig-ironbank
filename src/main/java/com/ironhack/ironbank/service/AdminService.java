package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AdminDTO;

import java.util.List;
public interface AdminService {

    AdminDTO createAdmin(AdminDTO adminDTO);
    AdminDTO findById(Long id);
    List<AdminDTO> findAll();
    AdminDTO update(Long id, AdminDTO adminDTO);
    void delete(Long id);
}
