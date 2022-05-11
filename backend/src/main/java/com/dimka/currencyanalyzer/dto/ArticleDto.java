package com.dimka.currencyanalyzer.dto;

import com.dimka.currencyanalyzer.model.ArticleSource;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
public class ArticleDto {

    private ArticleSource source;
    private String title;
    private String url;
    private Date date;
}
