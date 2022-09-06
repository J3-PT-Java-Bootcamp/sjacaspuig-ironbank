package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AdminDTO;

import javax.validation.Valid;
import java.util.List;
public interface AdminService {

    AdminDTO create(@Valid AdminDTO adminDTO);
    AdminDTO findById(@Valid Long id);
    List<AdminDTO> findAll();
    AdminDTO update(@Valid Long id, @Valid AdminDTO adminDTO);
    void delete(@Valid Long id);
}
