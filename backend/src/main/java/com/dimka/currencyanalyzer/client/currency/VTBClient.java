package com.dimka.currencyanalyzer.client.currency;

import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class VTBClient {

    private static final String CURRENCY_TRADE_KEY = "Выберите способ обмена";
    private static final String INTERNET_BANK_KEY = "В интернет-банке и мобильном банке";
    private static final String USD_KEY = "USD — Доллар США";
    private static final String EURO_KEY = "EUR — Евро";
    private static final String BUY_KEY = "Покупаем";
    private static final String SELL_KEY = "Продаем";

    @Value("${client.vtb.base-url}")
    private String url;

    @SneakyThrows
    public List<CurrencyFrame> getCurrencies() {
        ArrayList<CurrencyFrame> currencies = new ArrayList<>();
        RemoteWebDriver driver = new ChromeDriver();
        try {
            driver.get(url + "/personal/platezhi-i-perevody/obmen-valjuty/");
            driver.manage().timeouts().implicitlyWait(15000, TimeUnit.MILLISECONDS);
            Thread.sleep(10000);
            getElement(driver, "span", CURRENCY_TRADE_KEY).click();
            getElement(driver, "span", INTERNET_BANK_KEY).click();
            Thread.sleep(1500);
            currencies.add(getCurrency(driver, USD_KEY, CurrencyCode.USD));
            currencies.add(getCurrency(driver, EURO_KEY, CurrencyCode.EUR));
        } finally {
            driver.quit();
        }
        return currencies;
    }

    @SneakyThrows
    private WebElement getElement(RemoteWebDriver driver, String tag, String text) {
        WebElement element = driver.findElementsByTagName(tag).stream()
                .filter(el -> text.equals(el.getText()))
                .findFirst()
                .orElseThrow();
        driver.executeScript("arguments[0].scrollIntoView(false);", element);
        Thread.sleep(1500);
        return element;
    }

    private CurrencyFrame getCurrency(RemoteWebDriver driver, String key, CurrencyCode currencyCode) {
        WebElement currencyRow = driver.findElementsByTagName("div").stream()
                .filter(element -> key.equals(element.getText()))
                .findFirst()
                .orElseThrow()
                .findElement(By.xpath("./../.."));
        String buyPrice = currencyRow.findElements(By.tagName("span")).stream()
                .filter(webElement -> BUY_KEY.equals(webElement.getText()))
                .findFirst()
                .orElseThrow()
                .findElement(By.xpath("./.."))
                .findElement(By.tagName("div"))
                .findElement(By.tagName("p"))
                .getText()
                .replace(",", ".");
        String sellPrice = currencyRow.findElements(By.tagName("span")).stream()
                .filter(webElement -> SELL_KEY.equals(webElement.getText()))
                .findFirst()
                .orElseThrow()
                .findElement(By.xpath("./.."))
                .findElement(By.tagName("div"))
                .findElement(By.tagName("p"))
                .getText()
                .replace(",", ".");
        return new CurrencyFrame()
                .setCountry(Country.RUSSIA)
                .setBuyPrice(new BigDecimal(buyPrice))
                .setSellPrice(new BigDecimal(sellPrice))
                .setSource(Source.VTB)
                .setFirstCurrency(CurrencyCode.RUB)
                .setSecondCurrency(currencyCode)
                .setDate(Instant.now());
    }
}
