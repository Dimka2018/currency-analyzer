package com.dimka.currencyanalyzer.external.alfa.response;

import com.dimka.currencyanalyzer.external.alfa.CurrencyData;
import lombok.Data;

import java.util.List;

@Data
public class AlfaCurrencyResponse {

    private List<CurrencyData> data;
}
