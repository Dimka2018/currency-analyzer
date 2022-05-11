package com.dimka.currencyanalyzer.external.alfa.response;

import lombok.Data;

import java.util.List;

@Data
public class RateByClientType {

    private List<RateType> ratesByType;
}
