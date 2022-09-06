package com.ironhack.ironbank.dto;


import com.ironhack.ironbank.model.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoneyDTO {

    private Currency currency;
    private BigDecimal amount;

    public static MoneyDTO fromEntity(Money money) {
        var moneyDTO = new MoneyDTO();
        moneyDTO.setCurrency(money.getCurrency());
        moneyDTO.setAmount(money.getAmount());
        return moneyDTO;
    }
}