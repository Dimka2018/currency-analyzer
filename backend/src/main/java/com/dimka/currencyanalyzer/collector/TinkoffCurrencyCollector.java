package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.TinkoffClient;
import com.dimka.currencyanalyzer.external.tinkoff.IsoCode;
import com.dimka.currencyanalyzer.external.tinkoff.response.Category;
import com.dimka.currencyanalyzer.external.tinkoff.response.Currency;
import com.dimka.currencyanalyzer.external.tinkoff.response.TinkoffCurrencyResponse;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class TinkoffCurrencyCollector extends CurrencyCollector {

    private final TinkoffClient tinkoffClient;

    public TinkoffCurrencyCollector(CurrencyFrameService currencyFrameService, TinkoffClient tinkoffClient) {
        super(currencyFrameService);
        this.tinkoffClient = tinkoffClient;
    }

    @Override
    public List<CurrencyFrame> getCurrencies() {
        TinkoffCurrencyResponse usdResponse = tinkoffClient.getCurrencies(IsoCode.USD, IsoCode.RUB);
        TinkoffCurrencyResponse euroResponse = tinkoffClient.getCurrencies(IsoCode.EUR, IsoCode.RUB);
        return toCurrencyFrames(usdResponse, euroResponse);
    }

    private CurrencyFrame toCurrencyFrame(TinkoffCurrencyResponse response, CurrencyCode first, CurrencyCode second) {
        Currency currencyInfo = response.getPayload()
                .getRates()
                .stream()
                .filter(currency -> currency.getCategory().equals(Category.SAVING_ACCOUNT_TRANSFERS))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Currency not found"));
        return new CurrencyFrame()
                .setFirstCurrency(first)
                .setSecondCurrency(second)
                .setSource(Source.TINKOFF)
                .setCountry(Country.RUSSIA)
                .setDate(Instant.now())
                .setBuyPrice(currencyInfo.getBuy())
                .setSellPrice(currencyInfo.getSell());
    }

    private List<CurrencyFrame> toCurrencyFrames(TinkoffCurrencyResponse usdResponse, TinkoffCurrencyResponse euroResponse) {
        return List.of(
                toCurrencyFrame(usdResponse, CurrencyCode.RUB, CurrencyCode.USD),
                toCurrencyFrame(euroResponse, CurrencyCode.RUB, CurrencyCode.EUR)
        );
    }
}
