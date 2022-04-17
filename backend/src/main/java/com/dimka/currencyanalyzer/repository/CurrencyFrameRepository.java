package com.dimka.currencyanalyzer.repository;

import com.dimka.currencyanalyzer.model.CurrencyFrame;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CurrencyFrameRepository extends ReactiveCrudRepository<CurrencyFrame, Long> {
}
