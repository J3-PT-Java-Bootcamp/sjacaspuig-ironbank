package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.response.AdminCreateResponse;
import com.ironhack.ironbank.dto.AdminDTO;

import javax.validation.Valid;
import java.util.List;


public interface AdminService {

    AdminCreateResponse create(@Valid AdminDTO adminDTO);
    AdminDTO findById(@Valid String id);
    List<AdminDTO> findAll();
    AdminDTO update(@Valid String id, @Valid AdminDTO adminDTO);
    void delete(@Valid String id);
}
