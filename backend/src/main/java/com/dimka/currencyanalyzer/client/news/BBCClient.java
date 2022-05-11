package com.dimka.currencyanalyzer.client.news;

import com.dimka.currencyanalyzer.model.Article;
import com.dimka.currencyanalyzer.model.ArticleSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BBCClient implements NewsClient {

    @Value("${news.bbc.base-url}")
    private String url;

    @SneakyThrows
    @Override
    public Flux<Article> getNews() {
        Elements elements = Jsoup.connect(url + "/news").get()
                .select("#latest-stories-tab-container div a.gs-c-promo-heading");
        return Flux.fromIterable(elements)
                .map(this::toArticle);
    }

    @Override
    public Flux<Article> getNews(List<LocalDate> dates) {
        return Flux.empty();
    }

    private Article toArticle(Element element) {
        String link = element.attr("href").replace(url + "/", "");
        String title = element.text().replace("LiveLive", "");
        return new Article()
                .setUrl(url + link)
                .setTitle(title)
                .setSource(ArticleSource.BBC)
                .setDate(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
    }
}
