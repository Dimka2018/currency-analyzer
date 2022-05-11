package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.SebClient;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SEBCurrencyCollector extends CurrencyCollector {

    private final SebClient sebClient;

    public SEBCurrencyCollector(CurrencyFrameService currencyFrameService, SebClient sebClient) {
        super(currencyFrameService);
        this.sebClient = sebClient;
    }

    @Override
    List<CurrencyFrame> getCurrencies() {
        return sebClient.getCurrencies();
    }
}
