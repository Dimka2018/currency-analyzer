package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.AliexpressClient;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AliexpressCurrencyCollector extends CurrencyCollector {

    private final AliexpressClient aliexpressClient;

    @Value("${client.aliexpress.enable:true}")
    private boolean enable;

    public AliexpressCurrencyCollector(CurrencyFrameService currencyFrameService, AliexpressClient aliexpressClient) {
        super(currencyFrameService);
        this.aliexpressClient = aliexpressClient;
    }

    @Override
    List<CurrencyFrame> getCurrencies() {
        return aliexpressClient.getCurrencies();
    }

    @Override
    boolean isEnable() {
        return enable;
    }
}
