package com.dimka.currencyanalyzer.external.gazprom.response;

import lombok.Data;

import java.util.List;

@Data
public class CurrencyData {

    private List<Currency> items;
}
