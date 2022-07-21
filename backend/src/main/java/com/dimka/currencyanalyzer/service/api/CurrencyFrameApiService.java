package com.dimka.currencyanalyzer.service.api;

import com.dimka.currencyanalyzer.dto.CurrencyFrameDto;
import com.dimka.currencyanalyzer.mapper.CurrencyFrameToCurrencyFrameDtoMapper;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class CurrencyFrameApiService {

    private final CurrencyFrameService currencyFrameService;

    private final CurrencyFrameToCurrencyFrameDtoMapper currencyFrameToCurrencyFrameDtoMapper;

    public Flux<CurrencyFrameDto> getLatestCurrencies() {
        return currencyFrameService.getLatestCurrencyFrames()
                .map(currencyFrameToCurrencyFrameDtoMapper::convert);
    }

    public Flux<CurrencyFrameDto> getLatestCurrencies(Source source, Country country, CurrencyCode firstCurrency, CurrencyCode secondCurrency) {
        return currencyFrameService.getLatestCurrencyFrames(source, country, firstCurrency, secondCurrency)
                .map(currencyFrameToCurrencyFrameDtoMapper::convert)
                .sort(Comparator.comparing(CurrencyFrameDto::getDate));
    }

    @SneakyThrows
    public byte[] getZipHistory() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(bos)) {
                List<CurrencyFrame> fxRates = currencyFrameService.findAll().collectList().share().block();
                zipOutputStream.putNextEntry(new ZipEntry("fxRates.json"));
                zipOutputStream.write(asBytes(fxRates));
                zipOutputStream.closeEntry();
            }
            return bos.toByteArray();
        }
    }

    @SneakyThrows
    public void applyHistory(MultipartFile file) {
        try (ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream())) {
            File tempFile = File.createTempFile("fxRates", null);
            file.transferTo(tempFile);
            try (ZipFile zipFile = new ZipFile(tempFile)) {
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    byte[] buffer = zipFile.getInputStream(zipEntry).readAllBytes();
                    List<CurrencyFrame> fxRates = Arrays.stream(toFxRates(buffer))
                            .collect(Collectors.toList());
                    currencyFrameService.save(fxRates).subscribe();
                }
            }
            tempFile.delete();
        }
    }

    @SneakyThrows
    private CurrencyFrame[] toFxRates(byte[] content) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(content, CurrencyFrame[].class);
    }

    @SneakyThrows
    private <T> byte[] asBytes(T t) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsBytes(t);
    }

}
