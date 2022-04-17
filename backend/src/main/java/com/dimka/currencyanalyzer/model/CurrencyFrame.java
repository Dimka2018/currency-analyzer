package com.dimka.currencyanalyzer.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Accessors(chain = true)
@Data
@Table("CURRENCY_FRAME")
public class CurrencyFrame {

    @Id
    private Long id;

}
