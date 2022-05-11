package com.dimka.currencyanalyzer.external.alfa.response;

import lombok.Data;

@Data
public class Currency {

    private CurrencyValue buy;
    private CurrencyValue sell;
}
