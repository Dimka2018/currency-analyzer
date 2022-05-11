package com.dimka.currencyanalyzer.client.news;

import com.dimka.currencyanalyzer.model.Article;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

public interface NewsClient {

    Flux<Article> getNews();

    Flux<Article> getNews(List<LocalDate> dates);
}
