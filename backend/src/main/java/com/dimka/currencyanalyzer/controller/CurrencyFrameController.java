package com.dimka.currencyanalyzer.controller;

import com.dimka.currencyanalyzer.dto.CurrencyFrameDto;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.service.api.CurrencyFrameApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping(value = "/currencies/history/zip", produces="application/zip")
    public Mono<byte[]> downloadArticles() {
        return currencyFrameApiService.getZipHistory();
    }

    @PostMapping("/currencies/history/zip")
    public void applyHistory(MultipartFile file) {
        currencyFrameApiService.applyHistory(file);
    }
}
