package com.dimka.currencyanalyzer.external.delfi.response;

import lombok.Data;

import java.util.List;

@Data
public class DelfiResponseArticles {

    private List<DelfiResponseItem> items;
}
