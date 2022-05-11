package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.external.sber.response.SberCurrencyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SberClient {

    @Value("${client.sber.base-url}")
    private String url;

    @SneakyThrows
    public SberCurrencyResponse getCurrencies() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/driver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        ChromeDriver driver = new ChromeDriver(options);
        String data;
        try {
            driver.get(url + "/proxy/services/rates/public/actual?rateType=ERNP-2&isoCodes[]=USD&isoCodes[]=EUR");
            driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
            data = driver.findElement(By.tagName("pre")).getText();
        } finally {
            driver.quit();
        }
        return new ObjectMapper().readValue(data, SberCurrencyResponse.class);
    }
}
