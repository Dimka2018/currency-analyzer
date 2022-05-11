package com.dimka.currencyanalyzer.service;

import com.dimka.currencyanalyzer.client.news.NewsClient;
import com.dimka.currencyanalyzer.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class NewsService {

    private final List<NewsClient> clients;

    public Flux<Article> getNews() {
        return Flux.fromIterable(clients)
                .map(NewsClient::getNews)
                .flatMap(Function.identity());
    }

    public Flux<Article> getNews(List<LocalDate> dates) {
        return Flux.fromIterable(clients)
                .map(client -> client.getNews(dates))
                .flatMap(Function.identity());
    }

}
