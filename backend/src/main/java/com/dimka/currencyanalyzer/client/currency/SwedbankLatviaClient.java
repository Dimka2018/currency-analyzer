package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class SwedbankLatviaClient {

    @Value("${client.swedbank-latvia.base-url}")
    private String url;

    @SneakyThrows
    public List<CurrencyFrame> getCurrencies() {
        Document document = Jsoup.connect(url + "/private/d2d/payments2/rates/currency").get();
        CurrencyFrame usdCurrency = getCurrency(document, CurrencyCode.USD);
        return List.of(usdCurrency);
    }

    private CurrencyFrame getCurrency(Document document, CurrencyCode currencyCode) {
        Element element = document.selectFirst("tr[data-currency=\"" + currencyCode + "\"]");
        String sell = element.selectFirst("td:nth-child(2)").text();
        String buy = element.selectFirst("td:nth-child(3)").text();
        return new CurrencyFrame()
                .setFirstCurrency(CurrencyCode.EUR)
                .setSecondCurrency(currencyCode)
                .setBuyPrice(new BigDecimal(buy))
                .setSellPrice(new BigDecimal(sell))
                .setSource(Source.SWEDBANK)
                .setCountry(Country.LATVIA)
                .setDate(Instant.now());
    }
}
