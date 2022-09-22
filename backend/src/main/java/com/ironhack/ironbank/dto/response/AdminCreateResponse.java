package com.ironhack.ironbank.dto.response;

import com.ironhack.ironbank.dto.AdminDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AdminCreateResponse {

        private int status;
        private AdminDTO admin;
        private String message;

}
