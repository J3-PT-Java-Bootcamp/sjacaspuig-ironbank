package com.ironhack.ironbank.dto.response;

import com.ironhack.ironbank.dto.ThirdPartyDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ThirdPartyCreateResponse {

    private int status;
    private ThirdPartyDTO thirdParty;
    private String message;
}
