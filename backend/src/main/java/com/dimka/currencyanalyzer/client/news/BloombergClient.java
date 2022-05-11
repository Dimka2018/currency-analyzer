package com.dimka.currencyanalyzer.client.news;

import com.dimka.currencyanalyzer.model.Article;
import com.dimka.currencyanalyzer.model.ArticleSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class BloombergClient implements NewsClient {

    @Value("${news.bloomberg.base-url}")
    private String url;

    @SneakyThrows
    @Override
    public Flux<Article> getNews() {
        Elements elements = Jsoup.connect(url).get().select("article a");
        return Flux.fromIterable(elements)
                .filter(element -> StringUtils.isNotBlank(element.text()))
                .filter(element -> element.attr("href").startsWith("/news/articles"))
                .map(this::toArticle);
    }

    @Override
    public Flux<Article> getNews(List<LocalDate> dates) {
        return Flux.empty();
    }

    private Article toArticle(Element element) {
        String link = element.attr("href");
        String title = element.text();
        return new Article()
                .setUrl(url + link)
                .setTitle(title)
                .setSource(ArticleSource.BLOOMBERG)
                .setDate(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
    }
}
