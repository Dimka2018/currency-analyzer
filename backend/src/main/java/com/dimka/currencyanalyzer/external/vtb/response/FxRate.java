package com.dimka.currencyanalyzer.external.vtb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FxRate {

    @JsonProperty("currency1")
    private FxRateDescription from;

    @JsonProperty("currency2")
    private FxRateDescription to;
    private BigDecimal bid;
    private BigDecimal offer;
}
