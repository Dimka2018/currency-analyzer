package com.dimka.currencyanalyzer.mapper;

import com.dimka.currencyanalyzer.dto.CurrencyFrameDto;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class CurrencyFrameToCurrencyFrameDtoMapper {

    private static final BigDecimal CRITICAL_VALUE = BigDecimal.valueOf(1.1);

    public CurrencyFrameDto convert(CurrencyFrame frame) {
        return new CurrencyFrameDto()
                .setSellPrice(frame.getSellPrice())
                .setBuyPrice(frame.getBuyPrice())
                .setSource(frame.getSource())
                .setCountry(frame.getCountry())
                .setFirstCurrency(frame.getFirstCurrency())
                .setSecondCurrency(frame.getSecondCurrency())
                .setSpread(frame.getSellPrice().subtract(frame.getBuyPrice()))
                .setDate(Date.from(frame.getDate()))
                .setCriticalMovingAverage(frame.getMovingAverage().multiply(CRITICAL_VALUE))
                .setCritical(frame.getMovingAverage().multiply(CRITICAL_VALUE).compareTo(frame.getSellPrice().subtract(frame.getBuyPrice())) <= 0);
    }

}
