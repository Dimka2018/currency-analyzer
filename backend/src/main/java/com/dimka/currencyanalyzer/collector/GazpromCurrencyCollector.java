package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.GazpromClient;
import com.dimka.currencyanalyzer.external.gazprom.IsoCode;
import com.dimka.currencyanalyzer.external.gazprom.response.Currency;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GazpromCurrencyCollector extends CurrencyCollector {

    private final GazpromClient gazpromClient;

    public GazpromCurrencyCollector(CurrencyFrameService currencyFrameService, GazpromClient gazpromClient) {
        super(currencyFrameService);
        this.gazpromClient = gazpromClient;
    }


    @Override
    public List<CurrencyFrame> getCurrencies() {
        return gazpromClient.getCurrencies()
                .stream()
                .filter(r -> r.getContent() != null)
                .collect(Collectors.toList())
                .get(0)
                .getContent().get(0)
                .getItems().stream()
                .filter(item -> isActual(item.getTicker()))
                .map(this::toCurrencyFrame)
                .collect(Collectors.toList());
    }

    private CurrencyFrame toCurrencyFrame(Currency currency) {
        return new CurrencyFrame()
                .setFirstCurrency(CurrencyCode.RUB)
                .setSecondCurrency(CurrencyCode.valueOf(currency.getTicker().name()))
                .setSource(Source.GAZPROM)
                .setCountry(Country.RUSSIA)
                .setDate(Instant.now())
                .setBuyPrice(currency.getBuy())
                .setSellPrice(currency.getSell());
    }


    private boolean isActual(IsoCode isoCode) {
        return Arrays.stream(CurrencyCode.values())
                .map(CurrencyCode::name)
                .toList()
                .contains(isoCode.name());
    }
}
