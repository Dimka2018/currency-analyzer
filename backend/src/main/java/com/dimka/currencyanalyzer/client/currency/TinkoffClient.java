package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.external.tinkoff.IsoCode;
import com.dimka.currencyanalyzer.external.tinkoff.response.TinkoffCurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tinkoff-client", url = "${client.tinkoff.base-url}")
public interface TinkoffClient {

    @GetMapping("/v1/currency_rates")
    TinkoffCurrencyResponse getCurrencies(@RequestParam("from") IsoCode from,
                                          @RequestParam("to") IsoCode to);
}
