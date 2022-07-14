package com.dimka.currencyanalyzer.external.euronews;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Accessors(chain = true)
@Data
public class EuronewsArticle {

    private String title;
    private String fullUrl;
    private Instant date;
}
