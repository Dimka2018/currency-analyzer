package com.dimka.currencyanalyzer.controller;

import com.dimka.currencyanalyzer.dto.ArticleDto;
import com.dimka.currencyanalyzer.service.api.NewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class NewsController {

    private final NewsApiService newsApiService;

    @GetMapping("/news")
    public Flux<ArticleDto> getNews(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Set<Date> dates) {
        if (dates == null) {
            return newsApiService.getNews();
        }
        return newsApiService.getNews(dates);
    }
}
