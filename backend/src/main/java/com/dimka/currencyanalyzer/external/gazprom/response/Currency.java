package com.dimka.currencyanalyzer.external.gazprom.response;

import com.dimka.currencyanalyzer.external.gazprom.IsoCode;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Currency {

    private IsoCode ticker;
    private BigDecimal sell;
    private BigDecimal buy;

}
