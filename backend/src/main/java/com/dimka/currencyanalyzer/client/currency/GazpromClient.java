package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.external.gazprom.response.GazpromCurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "gazprom", url = "${client.gazprom.base-url}")
public interface GazpromClient {

    @GetMapping("/rest/exchange/rate?cityId=617&version=2&ab_version=original")
    List<GazpromCurrencyResponse> getCurrencies();

}
