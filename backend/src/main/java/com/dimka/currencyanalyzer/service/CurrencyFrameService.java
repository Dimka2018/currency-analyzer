package com.dimka.currencyanalyzer.service;

import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.repository.CurrencyFrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyFrameService {

    private final CurrencyFrameRepository currencyFrameRepository;

    public Mono<CurrencyFrame> save(CurrencyFrame currencyFrame) {
        return currencyFrameRepository.save(currencyFrame);
    }

    public Flux<CurrencyFrame> save(Iterable<CurrencyFrame> frames) {
        return currencyFrameRepository.saveAll(frames);
    }

    public Flux<CurrencyFrame> getLatestCurrencyFrames() {
        return currencyFrameRepository.findLatestFrames();
    }

    public Flux<CurrencyFrame> getLatestCurrencyFrames(Source source, Country country, CurrencyCode firstCurrency, CurrencyCode secondCurrency) {
        return currencyFrameRepository.findLastFrames(source, country, firstCurrency, secondCurrency);
    }

    public Mono<CurrencyFrame> getLatestCurrencyFrame(Source source, Country country, CurrencyCode firstCurrency, CurrencyCode secondCurrency) {
        return currencyFrameRepository.findLatestFrame(source, country, firstCurrency, secondCurrency);
    }

    public Flux<CurrencyFrame> getFxRate(Source source, Country country,
                                                   CurrencyCode firstCurrency, CurrencyCode secondCurrency,
                                                   Instant date) {
        return currencyFrameRepository.findFxRate(source, country, firstCurrency, secondCurrency, date);
    }

    public Mono<Long> countLatestCurrencyFrames(Source source, Country country, CurrencyCode firstCurrency, CurrencyCode secondCurrency) {
        return currencyFrameRepository.countLastFrames(source, country, firstCurrency, secondCurrency);
    }

    public Flux<CurrencyFrame> findAll() {
        return currencyFrameRepository.findAll();
    }
}
