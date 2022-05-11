package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.CitadeleClient;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CitadeleCurrencyCollector extends CurrencyCollector {

    private final CitadeleClient citadeleClient;

    public CitadeleCurrencyCollector(CurrencyFrameService currencyFrameService, CitadeleClient citadeleClient) {
        super(currencyFrameService);
        this.citadeleClient = citadeleClient;
    }

    @Override
    List<CurrencyFrame> getCurrencies() {
        return citadeleClient.getCurrencies();
    }
}
