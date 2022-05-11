package com.dimka.currencyanalyzer.collector;

import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
public abstract class CurrencyCollector {

    private static final long COLLECT_RATE = 3000000L;

    private final CurrencyFrameService currencyFrameService;

    @SneakyThrows
    @Scheduled(fixedRate = COLLECT_RATE)
    private void collect() {
        if (isEnable()) {
            getCurrencies().stream()
                    .filter(this::isNew)
                    .map(this::fillMovingAverage)
                    .forEach(frame -> currencyFrameService.save(frame).block());
        }
    }

    abstract List<CurrencyFrame> getCurrencies();

    boolean isEnable() {
        return true;
    }

    private CurrencyFrame fillMovingAverage(CurrencyFrame frame) {
        long count = currencyFrameService.countLatestCurrencyFrames(frame.getSource(),
                frame.getCountry(),
                frame.getFirstCurrency(),
                frame.getSecondCurrency())
                .blockOptional()
                .orElseThrow() + 1;
        Flux<CurrencyFrame> frames = currencyFrameService.getLatestCurrencyFrames(frame.getSource(), frame.getCountry(), frame.getFirstCurrency(), frame.getSecondCurrency());
        BigDecimal average = Flux.concat(frames, Flux.just(frame))
                .map(f -> f.getSellPrice().subtract(f.getBuyPrice()))
                .reduce(BigDecimal::add)
                .map(sum -> sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP))
                .block();

        return frame.setMovingAverage(average);
    }

    private boolean isNew(CurrencyFrame frame) {
        CurrencyFrame latestUsdCurrencyFrame = currencyFrameService.getLatestCurrencyFrame(frame.getSource(),
                frame.getCountry(), frame.getFirstCurrency(), frame.getSecondCurrency()).block();
        return latestUsdCurrencyFrame == null ||
                !latestUsdCurrencyFrame.getBuyPrice().equals(frame.getBuyPrice()) ||
                !latestUsdCurrencyFrame.getSellPrice().equals(frame.getSellPrice());
    }

}
