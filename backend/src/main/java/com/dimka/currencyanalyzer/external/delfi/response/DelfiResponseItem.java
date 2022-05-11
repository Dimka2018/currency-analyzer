package com.dimka.currencyanalyzer.external.delfi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DelfiResponseItem {

    private String title;

    @JsonProperty("web_url")
    private String url;
}
