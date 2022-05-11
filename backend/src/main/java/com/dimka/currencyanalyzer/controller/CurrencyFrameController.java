package com.dimka.currencyanalyzer.controller;

import com.dimka.currencyanalyzer.dto.CurrencyFrameDto;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.service.api.CurrencyFrameApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CurrencyFrameController {

    private final CurrencyFrameApiService currencyFrameApiService;

    @GetMapping("/currencies/frames")
    public Flux<CurrencyFrameDto> getLatestCurrencyFrames() {
        return currencyFrameApiService.getLatestCurrencies();
    }

    @GetMapping("/currencies/frames/latest")
    public Flux<CurrencyFrameDto> getLatestCurrencyFrame(@RequestParam Source source,
                                                         @RequestParam Country country,
                                                         @RequestParam CurrencyCode firstCurrency,
                                                         @RequestParam CurrencyCode secondCurrency) {
        return currencyFrameApiService.getLatestCurrencies(source, country, firstCurrency, secondCurrency);
    }
}
