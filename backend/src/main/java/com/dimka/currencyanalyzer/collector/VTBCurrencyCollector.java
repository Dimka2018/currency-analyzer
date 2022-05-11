package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.VTBClient;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class VTBCurrencyCollector extends CurrencyCollector {

    private final VTBClient vtbClient;

    @Value("${client.vtb.enable:true}")
    private boolean enable;

    public VTBCurrencyCollector(CurrencyFrameService currencyFrameService, VTBClient vtbClient) {
        super(currencyFrameService);
        this.vtbClient = vtbClient;
    }

    @Override
    public List<CurrencyFrame> getCurrencies() {
        return vtbClient.getCurrencies();
    }

    @Override
    boolean isEnable() {
        return enable;
    }
}
