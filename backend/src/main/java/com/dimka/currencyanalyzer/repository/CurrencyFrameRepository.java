package com.dimka.currencyanalyzer.repository;

import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyFrameRepository extends ReactiveCrudRepository<CurrencyFrame, Long> {

    @Query("""
            SELECT * FROM CURRENCY_FRAME WHERE
            SOURCE IN (SELECT DISTINCT SOURCE FROM CURRENCY_FRAME) AND
            DATE IN (SELECT MAX(DATE) FROM CURRENCY_FRAME GROUP BY (SOURCE, COUNTRY, FIRST_CURRENCY, SECOND_CURRENCY))
            """)
    Flux<CurrencyFrame> findLatestFrames();

    @Query("""
            SELECT * FROM CURRENCY_FRAME WHERE 
            SOURCE = :source AND
            COUNTRY = :country AND
            FIRST_CURRENCY = :firstCurrency AND
            SECOND_CURRENCY = :secondCurrency ORDER BY DATE DESC LIMIT 1
            """)
    Mono<CurrencyFrame> findLatestFrame(Source source, Country country, CurrencyCode firstCurrency, CurrencyCode secondCurrency);

    @Query("""
            SELECT * FROM CURRENCY_FRAME WHERE
            SOURCE = :source AND
            COUNTRY = :country AND
            FIRST_CURRENCY = :firstCurrency AND
            SECOND_CURRENCY = :secondCurrency ORDER BY DATE DESC LIMIT 100;
            """)
    Flux<CurrencyFrame> findLastFrames(Source source,
                                       Country country,
                                       CurrencyCode firstCurrency,
                                       CurrencyCode secondCurrency);

    @Query("""
            SELECT COUNT(*) FROM (SELECT * FROM CURRENCY_FRAME WHERE
                    SOURCE = :source AND
                    COUNTRY = :country AND
                    FIRST_CURRENCY = :firstCurrency AND
                    SECOND_CURRENCY = :secondCurrency ORDER BY DATE DESC LIMIT 100) as "count"
            """)
    Mono<Long> countLastFrames(Source source,
                               Country country,
                               CurrencyCode firstCurrency,
                               CurrencyCode secondCurrency);

}
