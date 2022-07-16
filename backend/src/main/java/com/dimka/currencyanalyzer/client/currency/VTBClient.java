package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.external.vtb.response.VTBCurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "vtb-client", url = "${client.vtb.base-url}")
public interface VTBClient {
    @GetMapping(value = "/api/currencyrates/table?category=2&type=1")
    VTBCurrencyResponse getFxRates();
}
