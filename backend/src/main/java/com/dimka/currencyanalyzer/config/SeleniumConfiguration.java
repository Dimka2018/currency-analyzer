package com.dimka.currencyanalyzer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SeleniumConfiguration {

    @Value("${selenium.driver-path}")
    private String driverPath;

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", driverPath);
    }
}
