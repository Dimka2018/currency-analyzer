package com.dimka.currencyanalyzer.service.api;

import com.dimka.currencyanalyzer.dto.CurrencyFrameDto;
import com.dimka.currencyanalyzer.mapper.CurrencyFrameToCurrencyFrameDtoMapper;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@RequiredArgsConstructor
@Service
public class CurrencyFrameApiService {

    private final CurrencyFrameService currencyFrameService;

    private final CurrencyFrameToCurrencyFrameDtoMapper currencyFrameToCurrencyFrameDtoMapper;

    public Flux<CurrencyFrameDto> getLatestCurrencies() {
        return currencyFrameService.getLatestCurrencyFrames()
                .map(currencyFrameToCurrencyFrameDtoMapper::convert);
    }

    public Flux<CurrencyFrameDto> getLatestCurrencies(Source source, Country country, CurrencyCode firstCurrency, CurrencyCode secondCurrency) {
        return currencyFrameService.getLatestCurrencyFrames(source, country, firstCurrency, secondCurrency)
                .map(currencyFrameToCurrencyFrameDtoMapper::convert)
                .sort(Comparator.comparing(CurrencyFrameDto::getDate));
    }

}
