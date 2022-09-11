package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.user.ThirdParty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThirdPartyDTO extends UserDTO {

        private String hashedKey;

        public static ThirdPartyDTO fromEntity(ThirdParty thirdParty) {
            var thirdPartyDTO = new ThirdPartyDTO();
            thirdPartyDTO.setId(thirdParty.getId());
            thirdPartyDTO.setFirstName(thirdParty.getFirstName());
            thirdPartyDTO.setHashedKey(thirdParty.getHashedKey());
            return thirdPartyDTO;
        }

        public static List<ThirdPartyDTO> fromEntities(List<ThirdParty> thirdParties) {
            return thirdParties.stream().map(ThirdPartyDTO::fromEntity).toList();
        }
}
