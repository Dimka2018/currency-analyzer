package com.dimka.currencyanalyzer.external.vtb.response;

import lombok.Data;

import java.util.List;

@Data
public class VTBCurrencyResponse {

    private List<FxRate> rates;
}
