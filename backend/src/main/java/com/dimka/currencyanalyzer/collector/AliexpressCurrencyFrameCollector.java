package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.client.AliexpressClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Component
public class AliexpressCurrencyFrameCollector {

    private static final long COLLECT_RATE = 1000L;

    private final AliexpressClient aliexpressClient;

    @Scheduled(fixedRate = COLLECT_RATE)
    public void collect() {
        BigDecimal o = aliexpressClient.getEuroPrice();
    }
}
