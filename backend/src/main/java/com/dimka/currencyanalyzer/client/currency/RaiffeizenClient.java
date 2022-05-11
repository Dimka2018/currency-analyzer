package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.external.raiffeizen.IsoCode;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaiffeizenClient {

    @Value("${client.raiffeizen.base-url}")
    private String url;

    @SneakyThrows
    public List<CurrencyFrame> getCurrencies() {
        Document document = Jsoup.connect(url + "/currency_rates/").get();
        return Arrays.stream(IsoCode.values())
                .map(code -> extractCurrencies(document, "#online .b-table__row", code))
                .collect(Collectors.toList());
    }

    private CurrencyFrame extractCurrencies(Document document, String selector, IsoCode code) {
        Element element = document.select(selector).stream()
                .filter(e -> code.name().equals(e.selectFirst(".b-table__td").text()))
                .findFirst().get();
        BigDecimal buy = new BigDecimal(element.selectFirst(":nth-child(4)").text());
        BigDecimal sell = new BigDecimal(element.selectFirst(":nth-child(5)").text());
        return new CurrencyFrame()
                .setFirstCurrency(CurrencyCode.RUB)
                .setSecondCurrency(CurrencyCode.valueOf(code.name()))
                .setSource(Source.RAIFFEIZEN)
                .setCountry(Country.RUSSIA)
                .setDate(Instant.now())
                .setBuyPrice(buy)
                .setSellPrice(sell);
    }
}
