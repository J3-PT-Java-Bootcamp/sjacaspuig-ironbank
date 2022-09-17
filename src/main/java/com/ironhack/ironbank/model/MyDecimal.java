package com.ironhack.ironbank.model;

import com.ironhack.ironbank.dto.MyDecimalDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@Getter
@Setter
public class MyDecimal {

    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    private static final int DEFAULT_SCALE = 5;

    @NotNull
    @Column(name = "value", precision=19, scale=5)
    private BigDecimal value;
    
    public MyDecimal(BigDecimal value) {
        setValue(value.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public MyDecimal(String value) {
        setValue(new BigDecimal(value).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public MyDecimal(int value) {
        setValue(new BigDecimal(value).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public MyDecimal() {
    }
    
    public BigDecimal increase(MyDecimal myDecimal) {
        setValue(this.value.add(myDecimal.value));
        return this.value;
    }
    
    public BigDecimal increase(BigDecimal addDecimal) {
        setValue(this.value.add(addDecimal));
        return this.value;
    }
    
    public BigDecimal decrease(MyDecimal myDecimal) {
        setValue(this.value.subtract(myDecimal.value));
        return this.value;
    }
    
    public BigDecimal decrease(BigDecimal subtractDecimal) {
        setValue(this.value.subtract(subtractDecimal));
        return this.value;
    }
    
    public int compareTo(MyDecimal myDecimal) {
        return this.value.compareTo(myDecimal.value);
    }

    public MyDecimal multiply(MyDecimal myDecimal) {
        return new MyDecimal(this.value.multiply(myDecimal.value));
    }

    public MyDecimal divide(MyDecimal myDecimal) {
        return new MyDecimal(this.value.divide(myDecimal.value));
    }

    public String toString() {
        return getValue().toString();
    }
    
    public MyDecimal fromDTO(MyDecimalDTO myDecimalDTO) {
        return new MyDecimal(myDecimalDTO.getValue());
    }
}
