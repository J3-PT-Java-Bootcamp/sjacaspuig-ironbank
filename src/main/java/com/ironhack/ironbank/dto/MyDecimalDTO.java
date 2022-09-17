package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.MyDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@Getter
@Setter
public class MyDecimalDTO {

    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    private static final int DEFAULT_SCALE = 5;

    private BigDecimal value;

    public MyDecimalDTO(BigDecimal value) {
        setValue(value.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public MyDecimalDTO fromEntity(MyDecimal myDecimal) {
        return new MyDecimalDTO(myDecimal.getValue());
    }
}
