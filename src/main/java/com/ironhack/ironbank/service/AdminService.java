package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AdminDTO;

import javax.validation.Valid;
import java.util.List;


public interface AdminService {

    AdminDTO create(@Valid AdminDTO adminDTO, String password);
    AdminDTO findById(@Valid String id);
    List<AdminDTO> findAll();
    AdminDTO update(@Valid String id, @Valid AdminDTO adminDTO);
    void delete(@Valid String id);
}
