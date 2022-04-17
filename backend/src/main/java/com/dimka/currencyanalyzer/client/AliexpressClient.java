package com.dimka.currencyanalyzer.client;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class AliexpressClient {

    @Value("${client.aliexpress.url}")
    private String url;

    @SneakyThrows
    public BigDecimal getEuroPrice() {
        Document doc = Jsoup.connect(url).get();
        Elements select = doc.select(".product-price-value");
        String collect = select.stream().map(Element::text).collect(Collectors.joining(""));
        return BigDecimal.ZERO;
    }
}
