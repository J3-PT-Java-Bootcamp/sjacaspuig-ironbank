package com.ironhack.ironbank.dto;


import com.ironhack.ironbank.model.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MoneyDTO {

    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    private static final Currency EUR = Currency.getInstance("EUR");
    private Currency currency;
    private BigDecimal amount;

    public MoneyDTO(Money money) {
        this.currency = EUR;
        this.amount = money.getAmount().setScale(money.getCurrency().getDefaultFractionDigits(), DEFAULT_ROUNDING);
    }

    public static MoneyDTO fromEntity(Money money) {
        return new MoneyDTO(money);
    }

    public static List<MoneyDTO> fromEntities(List<Money> moneys) {
        return moneys.stream().map(MoneyDTO::fromEntity).toList();
    }
}
