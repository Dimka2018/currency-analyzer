package com.dimka.currencyanalyzer.external.gazprom.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class GazpromCurrencyResponse {

    private List<CurrencyData> content;
}
