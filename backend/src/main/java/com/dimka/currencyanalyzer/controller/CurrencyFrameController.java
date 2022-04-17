package com.dimka.currencyanalyzer.controller;

import com.dimka.currencyanalyzer.model.CurrencyFrame;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class CurrencyFrameController {

    public Flux<CurrencyFrame> getActualCurrencyFrames() {
        return Flux.empty();
    }
}
