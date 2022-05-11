package com.dimka.currencyanalyzer.service.api;

import com.dimka.currencyanalyzer.dto.ArticleDto;
import com.dimka.currencyanalyzer.mapper.ArticleToArticleDtoMapper;
import com.dimka.currencyanalyzer.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewsApiService {

    private final NewsService newsService;
    private final ArticleToArticleDtoMapper articleToArticleDtoMapper;

    public Flux<ArticleDto> getNews() {
        return newsService.getNews()
                .map(articleToArticleDtoMapper::convert);
    }

    public Flux<ArticleDto> getNews(Set<Date> dates) {
        List<LocalDate> localDates = dates.stream()
                .map(date -> Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                .collect(Collectors.toList());
        return newsService.getNews(localDates)
                .map(articleToArticleDtoMapper::convert);
    }
}
