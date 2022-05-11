package com.dimka.currencyanalyzer.external.delfi;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class DelfiRequestVariables {

    private String channelsExternalId;
    private String publishTimeFrom;
    private String publishTimeTo;
    private int limit;
}
