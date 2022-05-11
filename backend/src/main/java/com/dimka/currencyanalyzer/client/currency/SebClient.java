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
public class SebClient {

    @Value("${client.seb.base-url}")
    private String url;

    @SneakyThrows
    public List<CurrencyFrame> getCurrencies() {
        Document document = Jsoup.connect(url + "/ru/chastnym-licam/povsednevnye-uslugi/raschety/kursy-valyut").get();
        return List.of(getCurrency(document, CurrencyCode.USD));
    }

    private CurrencyFrame getCurrency(Document document, CurrencyCode currencyCode) {
        Element element = document.selectFirst("tr[data-crc=\"" + currencyCode + "\"]");
        String sell = element.selectFirst("td[data-transfer-buy]").text().replace(",", ".");
        String buy = element.selectFirst("td[data-transfer-sell]").text().replace(",", ".");
        return new CurrencyFrame()
                .setFirstCurrency(CurrencyCode.EUR)
                .setSecondCurrency(currencyCode)
                .setBuyPrice(new BigDecimal(buy))
                .setSellPrice(new BigDecimal(sell))
                .setSource(Source.SEB)
                .setCountry(Country.LATVIA)
                .setDate(Instant.now());
    }
}
