package com.dimka.currencyanalyzer.external.delfi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DelfiResponseData {

    @JsonProperty("getArticles")
    private DelfiResponseArticles articles;
}
