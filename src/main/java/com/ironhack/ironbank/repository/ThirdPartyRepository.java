package com.ironhack.ironbank.repository;

import com.ironhack.ironbank.model.user.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, String> {

}
