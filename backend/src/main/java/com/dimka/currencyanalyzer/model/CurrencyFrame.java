package com.dimka.currencyanalyzer.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Accessors(chain = true)
@Data
@Table("CURRENCY_FRAME")
public class CurrencyFrame {

    @Id
    private Long id;

    @Column("FIRST_CURRENCY")
    private CurrencyCode firstCurrency;

    @Column("SECOND_CURRENCY")
    private CurrencyCode secondCurrency;

    @Column("BUY_PRICE")
    private BigDecimal buyPrice;

    @Column("SELL_PRICE")
    private BigDecimal sellPrice;

    @Column("SOURCE")
    private Source source;

    @Column("COUNTRY")
    private Country country;

    @Column("DATE")
    private Instant date;

    @Column("MOVING_AVERAGE")
    private BigDecimal movingAverage;

}
