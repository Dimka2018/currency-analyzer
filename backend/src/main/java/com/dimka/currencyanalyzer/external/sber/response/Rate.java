package com.dimka.currencyanalyzer.external.sber.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
public class Rate {

    private BigDecimal rateSell;
    private BigDecimal rateBuy;
}
