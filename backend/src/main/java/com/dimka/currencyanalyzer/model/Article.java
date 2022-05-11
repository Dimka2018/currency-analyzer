package com.dimka.currencyanalyzer.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Date;

@Accessors(chain = true)
@Data
public class Article {

    private ArticleSource source;
    private String title;
    private String url;
    private Instant date;
}
