package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.Decimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@Getter
@Setter
public class DecimalDTO {

    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    private static final int DEFAULT_SCALE = 5;

    private BigDecimal decimal;

    public DecimalDTO(BigDecimal decimal) {
        setDecimal(decimal.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public DecimalDTO fromEntity(Decimal decimal) {
        return new DecimalDTO(decimal.getDecimal());
    }
}
