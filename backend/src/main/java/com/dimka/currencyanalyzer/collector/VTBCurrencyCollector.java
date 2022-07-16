package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.VTBClient;
import com.dimka.currencyanalyzer.external.vtb.response.FxRate;
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
import java.util.stream.Collectors;

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
        return vtbClient.getFxRates().getRates()
                .stream()
                .filter(rate -> rate.getFrom().getCode().equalsIgnoreCase("USD") || rate.getFrom().getCode().equalsIgnoreCase("EUR"))
                .map(this::toCurrencyFrame)
                .collect(Collectors.toList());
    }

    @Override
    boolean isEnable() {
        return enable;
    }

    private CurrencyFrame toCurrencyFrame(FxRate fxRate) {
        return new CurrencyFrame()
                .setFirstCurrency(CurrencyCode.RUB)
                .setSecondCurrency(CurrencyCode.valueOf(fxRate.getFrom().getCode()))
                .setBuyPrice(fxRate.getBid())
                .setSellPrice(fxRate.getOffer())
                .setSource(Source.VTB)
                .setCountry(Country.RUSSIA)
                .setDate(Instant.now());
    }
}
