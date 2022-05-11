package com.dimka.currencyanalyzer.external.tinkoff.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Currency {

    private String category;
    private BigDecimal buy;
    private BigDecimal sell;
}
