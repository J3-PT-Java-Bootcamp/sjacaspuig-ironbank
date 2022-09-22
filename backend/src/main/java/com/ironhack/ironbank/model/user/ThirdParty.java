package com.ironhack.ironbank.model.user;

import com.ironhack.ironbank.dto.ThirdPartyDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "third_parties")
public class ThirdParty extends User {

    @NotNull
    @Column(name = "hashed_key")
    private String hashedKey;

    public static ThirdParty fromDTO(ThirdPartyDTO thirdPartyDTO) {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(thirdPartyDTO.getId());
        thirdParty.setFirstName(thirdPartyDTO.getFirstName());
        thirdParty.setLastName(thirdPartyDTO.getLastName());
        thirdParty.setHashedKey(thirdPartyDTO.getHashedKey());
        return thirdParty;
    }
}
