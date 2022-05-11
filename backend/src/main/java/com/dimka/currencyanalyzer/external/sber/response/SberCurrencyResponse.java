package com.dimka.currencyanalyzer.external.sber.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
public class SberCurrencyResponse {

    @JsonProperty("EUR")
    private Currency euro;

    @JsonProperty("USD")
    private Currency usd;
}
