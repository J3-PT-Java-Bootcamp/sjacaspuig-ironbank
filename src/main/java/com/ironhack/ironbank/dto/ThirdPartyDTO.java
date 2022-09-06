package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.user.ThirdParty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThirdPartyDTO extends UserDTO {

        private String hashedKey;

        public static ThirdPartyDTO fromEntity(ThirdParty thirdParty) {
            var thirdPartyDTO = new ThirdPartyDTO();
            thirdPartyDTO.setId(thirdParty.getId());
            thirdPartyDTO.setName(thirdParty.getName());
            thirdPartyDTO.setHashedKey(thirdParty.getHashedKey());
            return thirdPartyDTO;
        }
}
