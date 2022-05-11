package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.currency.AlfaClient;
import com.dimka.currencyanalyzer.external.alfa.CurrencyData;
import com.dimka.currencyanalyzer.external.alfa.IsoCode;
import com.dimka.currencyanalyzer.external.alfa.response.AlfaCurrencyResponse;
import com.dimka.currencyanalyzer.external.alfa.response.Currency;
import com.dimka.currencyanalyzer.external.alfa.response.RateByClientType;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class AlfaCurrencyCollector extends CurrencyCollector {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+00:00");
    private final AlfaClient alfaClient;

    @Value("${client.alfa.enable:true}")
    private boolean enable;

    public AlfaCurrencyCollector(CurrencyFrameService currencyFrameService, AlfaClient alfaClient) {
        super(currencyFrameService);
        this.alfaClient = alfaClient;
    }

    @Override
    public List<CurrencyFrame> getCurrencies() {
        AlfaCurrencyResponse usdResponse = alfaClient.getCurrency(IsoCode.USD,
                    formatter.format(LocalDate.now().atStartOfDay()),
                    formatter.format(LocalDateTime.now()));
        AlfaCurrencyResponse euroResponse = alfaClient.getCurrency(IsoCode.EUR,
                formatter.format(LocalDate.now().atStartOfDay()),
                formatter.format(LocalDateTime.now()));
        return toCurrencyFrames(usdResponse, euroResponse);
    }

    @Override
    boolean isEnable() {
        return enable;
    }

    private List<CurrencyFrame> toCurrencyFrames(AlfaCurrencyResponse usdResponse, AlfaCurrencyResponse euroResponse) {
        Currency usdCurrency = extractCurrency(usdResponse, IsoCode.USD);
        Currency euroCurrency = extractCurrency(euroResponse, IsoCode.EUR);
        return List.of(
                new CurrencyFrame()
                        .setFirstCurrency(CurrencyCode.RUB)
                        .setSecondCurrency(CurrencyCode.USD)
                        .setSource(Source.ALFA)
                        .setCountry(Country.RUSSIA)
                        .setDate(Instant.now())
                        .setBuyPrice(usdCurrency.getBuy().getOriginalValue())
                        .setSellPrice(usdCurrency.getSell().getOriginalValue()),
                new CurrencyFrame()
                        .setFirstCurrency(CurrencyCode.RUB)
                        .setSecondCurrency(CurrencyCode.EUR)
                        .setSource(Source.ALFA)
                        .setCountry(Country.RUSSIA)
                        .setDate(Instant.now())
                        .setBuyPrice(euroCurrency.getBuy().getOriginalValue())
                        .setSellPrice(euroCurrency.getSell().getOriginalValue())
        );
    }

    private Currency extractCurrency(AlfaCurrencyResponse response, IsoCode isoCode) {
        return response.getData().stream()
                .filter(data -> data.getCurrencyCode() == isoCode)
                .map(CurrencyData::getRateByClientType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found currency for " + isoCode))
                .stream()
                .map(RateByClientType::getRatesByType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found currency for " + isoCode))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found currency for " + isoCode))
                .getLastActualRate();
    }
}
