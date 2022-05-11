package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.RaiffeizenClient;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RaiffeizenCurrencyCollector extends CurrencyCollector {

    private final RaiffeizenClient raiffeizenClient;

    public RaiffeizenCurrencyCollector(CurrencyFrameService currencyFrameService, RaiffeizenClient raiffeizenClient) {
        super(currencyFrameService);
        this.raiffeizenClient = raiffeizenClient;
    }

    @Override
    public List<CurrencyFrame> getCurrencies() {
        return raiffeizenClient.getCurrencies();
    }

}
