package com.dimka.currencyanalyzer.dto;

import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.Source;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Accessors(chain = true)
@Data
public class CurrencyFrameDto {

    private Source source;
    private Country country;
    private CurrencyCode firstCurrency;
    private CurrencyCode secondCurrency;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    private BigDecimal spread;
    private Date date;
    private BigDecimal criticalMovingAverage;
    private boolean critical;

}
