package com.dimka.currencyanalyzer.mapper;

import com.dimka.currencyanalyzer.dto.ArticleDto;
import com.dimka.currencyanalyzer.model.Article;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ArticleToArticleDtoMapper {

    public ArticleDto convert(Article article) {
        return new ArticleDto()
                .setSource(article.getSource())
                .setTitle(article.getTitle())
                .setUrl(article.getUrl())
                .setDate(new Date(article.getDate().toEpochMilli()));
    }
}
