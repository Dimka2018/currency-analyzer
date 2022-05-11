package com.dimka.currencyanalyzer.external.tinkoff.response;

import lombok.Data;

import java.util.List;

@Data
public class Payload {

    private List<Currency> rates;

}
