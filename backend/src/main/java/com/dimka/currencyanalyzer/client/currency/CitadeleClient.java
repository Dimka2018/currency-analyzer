package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class CitadeleClient {

    @Value("${client.citadele.base-url}")
    private String url;

    @SneakyThrows
    public List<CurrencyFrame> getCurrencies() {
        Document document = Jsoup.connect(url + "/ru/rates/").get();
        CurrencyFrame usdCurrency = getCurrency(document, CurrencyCode.USD);
        return List.of(usdCurrency);
    }

    private CurrencyFrame getCurrency(Document document, CurrencyCode currencyCode) {
        Element element = document.select("td.currency span.code").stream()
                .filter(el -> CurrencyCode.USD.name().equals(el.text()))
                .findFirst()
                .orElseThrow()
                .parent()
                .parent();
        String sell = element.selectFirst("td:nth-child(3)").text().replace(",", ".");
        String buy = element.selectFirst("td:nth-child(4)").text().replace(",", ".");
        return new CurrencyFrame()
                .setFirstCurrency(CurrencyCode.EUR)
                .setSecondCurrency(currencyCode)
                .setBuyPrice(new BigDecimal(buy))
                .setSellPrice(new BigDecimal(sell))
                .setSource(Source.CITADELE)
                .setCountry(Country.LATVIA)
                .setDate(Instant.now());
    }
}
