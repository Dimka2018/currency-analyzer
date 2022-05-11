package com.dimka.currencyanalyzer.external.alfa;

import com.dimka.currencyanalyzer.external.alfa.response.RateByClientType;
import com.dimka.currencyanalyzer.external.alfa.IsoCode;
import lombok.Data;

import java.util.List;

@Data
public class CurrencyData {

    private IsoCode currencyCode;
    private List<RateByClientType> rateByClientType;
}
