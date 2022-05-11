package com.dimka.currencyanalyzer.client.news;

import com.dimka.currencyanalyzer.external.delfi.DelfiNewsRequest;
import com.dimka.currencyanalyzer.external.delfi.DelfiRequestVariables;
import com.dimka.currencyanalyzer.external.delfi.response.DelfiNewsResponse;
import com.dimka.currencyanalyzer.model.Article;
import com.dimka.currencyanalyzer.model.ArticleSource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DelfiClient implements NewsClient {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${news.delfi.base-url}")
    private String url;

    @SneakyThrows
    @Override
    public Flux<Article> getNews() {
        LocalDate now = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();
        return Flux.fromIterable(getNews(now));
    }

    @Override
    public Flux<Article> getNews(List<LocalDate> dates) {
        return Flux.fromIterable(dates)
                .map(this::getNews)
                .flatMap(Flux::fromIterable);
    }

    private List<Article> getNews(LocalDate date) {
        DelfiNewsRequest request = new DelfiNewsRequest()
                .setVariables(new DelfiRequestVariables()
                        .setLimit(50)
                        .setChannelsExternalId("2")
                        .setPublishTimeFrom(formatter.format(date))
                        .setPublishTimeTo(formatter.format(date) + "T23:59:59.000Z"));
        ResponseEntity<DelfiNewsResponse> response = new RestTemplate().postForEntity(url + "/content/v2/graphql", request, DelfiNewsResponse.class);
        return response.getBody()
                .getData()
                .getArticles()
                .getItems()
                .stream()
                .map(item -> new Article()
                        .setDate(date.atStartOfDay().toInstant(ZoneOffset.UTC))
                        .setTitle(item.getTitle())
                        .setUrl(item.getUrl())
                        .setSource(ArticleSource.DELFI))
                .collect(Collectors.toList());
    }
}
