package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.external.alfa.IsoCode;
import com.dimka.currencyanalyzer.external.alfa.response.AlfaCurrencyResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@FeignClient(name = "alfa-client", url = "${client.alfa.base-url}")
public interface AlfaClient {

    @Headers("user-agent: ${network.header.user-agent}")
    @GetMapping(value = "/api/v1/scrooge/currencies/alfa-rates?clientType.eq=standardCC&lastActualForDate.eq=true&rateType.in=makeCash,cashOutAlfa")
    AlfaCurrencyResponse getCurrency(@RequestParam("currencyCode.eq") IsoCode currencyCode,
                                     @RequestParam("date.gte") String from,
                                     @RequestParam("date.lt") String to);
}
