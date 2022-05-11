package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.model.CurrencyFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AliexpressClient {

    @Value("${client.aliexpress.base-url}")
    private String url;

    public List<CurrencyFrame> getCurrencies() {
        return Collections.emptyList();
    }
}
