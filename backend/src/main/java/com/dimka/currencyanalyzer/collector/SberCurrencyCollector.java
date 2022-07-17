package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.SberClient;
import com.dimka.currencyanalyzer.external.sber.response.SberCurrencyResponse;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class SberCurrencyCollector extends CurrencyCollector {

    private final SberClient sberClient;

    @Value("${client.sber.enable:true}")
    private boolean enable;

    public SberCurrencyCollector(CurrencyFrameService currencyFrameService, SberClient sberClient) {
        super(currencyFrameService);
        this.sberClient = sberClient;
    }

    @Override
    public List<CurrencyFrame> getCurrencies() {
        SberCurrencyResponse response = sberClient.getCurrencies();
        return toCurrencyFrames(response);
    }

    private List<CurrencyFrame> toCurrencyFrames(SberCurrencyResponse response) {
        return List.of(
                new CurrencyFrame()
                        .setFirstCurrency(CurrencyCode.RUB)
                        .setSecondCurrency(CurrencyCode.USD)
                        .setSource(Source.SBER)
                        .setCountry(Country.RUSSIA)
                        .setDate(Instant.now())
                        .setBuyPrice(response.getUsd().getRateList().get(0).getRateBuy())
                        .setSellPrice(response.getUsd().getRateList().get(0).getRateSell()),
                new CurrencyFrame()
                        .setFirstCurrency(CurrencyCode.RUB)
                        .setSecondCurrency(CurrencyCode.EUR)
                        .setSource(Source.SBER)
                        .setCountry(Country.RUSSIA)
                        .setDate(Instant.now())
                        .setBuyPrice(response.getEuro().getRateList().get(0).getRateBuy())
                        .setSellPrice(response.getEuro().getRateList().get(0).getRateSell())
        );
    }

    @Override
    boolean isEnable() {
        return this.enable;
    }
}
