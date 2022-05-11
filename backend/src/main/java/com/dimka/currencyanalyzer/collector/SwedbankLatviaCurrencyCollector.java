package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.SwedbankLatviaClient;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SwedbankLatviaCurrencyCollector extends CurrencyCollector {

    private final SwedbankLatviaClient swedbankClient;

    public SwedbankLatviaCurrencyCollector(CurrencyFrameService currencyFrameService, SwedbankLatviaClient swedbankClient) {
        super(currencyFrameService);
        this.swedbankClient = swedbankClient;
    }

    @Override
    List<CurrencyFrame> getCurrencies() {
        return swedbankClient.getCurrencies();
    }
}
