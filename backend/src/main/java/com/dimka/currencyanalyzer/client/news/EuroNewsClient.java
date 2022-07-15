package com.dimka.currencyanalyzer.client.news;

import com.dimka.currencyanalyzer.external.euronews.EuronewsArticle;
import com.dimka.currencyanalyzer.model.Article;
import com.dimka.currencyanalyzer.model.ArticleSource;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EuroNewsClient implements NewsClient {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("/yyyy/MM/dd");

    @Value("${news.euronews.base-url}")
    private String url;

    @Override
    public Flux<Article> getNews() {
        EuronewsArticle[] articles = new RestTemplate().getForObject(url + "/api/timeline.json?limit=15", EuronewsArticle[].class);
        return Flux.fromArray(articles)
                .map(article -> article.setDate(Instant.now()))
                .map(this::toArticle);
    }

    @Override
    public Flux<Article> getNews(List<LocalDate> dates) {
        return Flux.fromIterable(dates)
                .map(this::getNews)
                .flatMap(Flux::fromIterable);
    }

    @SneakyThrows
    public List<Article> getNews(LocalDate date) {
        long pageCount = 1;
        Set<Article> articles = new HashSet<>();
        for (int i = 0; i < pageCount; i++) {
            Document document = Jsoup.connect(url + formatter.format(date) + "/?p=" + pageCount).get();
            pageCount = document.select(".c-paginator-li").size();
            List<Article> list = document.select("article .m-object__title__link[rel=\"bookmark\"]").stream()
                    .map(element -> new Article()
                            .setTitle(element.attr("title"))
                            .setUrl(element.attr("href"))
                            .setDate(date.atStartOfDay().toInstant(ZoneOffset.UTC))
                            .setSource(ArticleSource.EURONEWS))
                    .collect(Collectors.toList());
            articles.addAll(list);
        }
        return articles.stream().toList();
    }

    private Article toArticle(EuronewsArticle euronewsArticle) {
        return new Article()
                .setSource(ArticleSource.EURONEWS)
                .setTitle(euronewsArticle.getTitle())
                .setUrl(url + euronewsArticle.getFullUrl())
                .setDate(euronewsArticle.getDate());
    }
}
