package com.dimka.currencyanalyzer.client.news;

import com.dimka.currencyanalyzer.model.Article;
import com.dimka.currencyanalyzer.model.ArticleSource;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewYorkTimesClient implements NewsClient {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("/yyyy/MM/dd/");

    @Value("${news.new-york-times.base-url}")
    private String url;

    @SneakyThrows
    @Override
    public Flux<Article> getNews() {
        Elements elements = Jsoup.connect(url).get().select("section.story-wrapper a");
        return Flux.fromIterable(elements)
                .filter(element -> !element.text().startsWith("LIVE "))
                .filter(element -> !element.attr("href").startsWith(url + "/interactive"))
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
        Document document = Jsoup.connect(url + "/issue/todayspaper" + formatter.format(date) + "/todays-new-york-times").get();
        return document.select("section.story-wrapper a").stream()
                .filter(element -> !element.text().startsWith("LIVE "))
                .filter(element -> !element.attr("href").startsWith(url + "/interactive"))
                .map(this::toArticle)
                .map(article -> article.setDate(date.atStartOfDay().toInstant(ZoneOffset.UTC)))
                .collect(Collectors.toList());
    }

    private Article toArticle(Element element) {
        String link = element.attr("href");
        String title = element.text();
        return new Article()
                .setUrl(link)
                .setTitle(title)
                .setSource(ArticleSource.NEW_YORK_TIMES)
                .setDate(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
    }
}
