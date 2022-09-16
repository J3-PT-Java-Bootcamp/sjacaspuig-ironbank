package com.ironhack.ironbank.model;

import com.ironhack.ironbank.dto.DecimalDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@Getter
@Setter
public class Decimal {

    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    private static final int DEFAULT_SCALE = 5;

    @NotNull
    private BigDecimal decimal;
    
    public Decimal(BigDecimal decimal) {
        setDecimal(decimal.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public Decimal(String decimal) {
        setDecimal(new BigDecimal(decimal).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public Decimal(int decimal) {
        setDecimal(new BigDecimal(decimal).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING));
    }

    public Decimal() {
    }
    
    public BigDecimal increaseDecimal(Decimal decimal) {
        setDecimal(this.decimal.add(decimal.decimal));
        return this.decimal;
    }
    
    public BigDecimal increaseDecimal(BigDecimal addDecimal) {
        setDecimal(this.decimal.add(addDecimal));
        return this.decimal;
    }
    
    public BigDecimal decreaseDecimal(Decimal decimal) {
        setDecimal(this.decimal.subtract(decimal.decimal));
        return this.decimal;
    }
    
    public BigDecimal decreaseDecimal(BigDecimal subtractDecimal) {
        setDecimal(this.decimal.subtract(subtractDecimal));
        return this.decimal;
    }
    
    public int compareDecimal(Decimal decimal) {
        return this.decimal.compareTo(decimal.decimal);
    }

    public Decimal multiplyDecimal(Decimal decimal) {
        return new Decimal(this.decimal.multiply(decimal.decimal));
    }

    public String toString() {
        return getDecimal().toString();
    }
    
    public Decimal fromDTO(DecimalDTO decimalDTO) {
        return new Decimal(decimalDTO.getDecimal());
    }
}
