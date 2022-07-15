package com.dimka.currencyanalyzer.service.api;

import com.dimka.currencyanalyzer.dto.CurrencyFrameDto;
import com.dimka.currencyanalyzer.mapper.CurrencyFrameToCurrencyFrameDtoMapper;
import com.dimka.currencyanalyzer.model.Country;
import com.dimka.currencyanalyzer.model.CurrencyCode;
import com.dimka.currencyanalyzer.model.CurrencyFrame;
import com.dimka.currencyanalyzer.model.Source;
import com.dimka.currencyanalyzer.service.CurrencyFrameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
    public Mono<byte[]> getZipHistory() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(bos)) {
                return currencyFrameService.findAll()
                        .collect(Collectors.toList())
                        .map(this::asBytes)
                        .publishOn(Schedulers.boundedElastic())
                        .map(bytes -> {
                            try {
                                zipOutputStream.putNextEntry(new ZipEntry("fxRates.json"));
                                zipOutputStream.write(bytes);
                                zipOutputStream.closeEntry();
                            } catch (Exception e) {
                                throw new RuntimeException("Can't parse history");
                            }
                            return bos.toByteArray();
                        });
            }
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
        return new ObjectMapper().readValue(content, CurrencyFrame[].class);
    }

    @SneakyThrows
    private <T> byte[] asBytes(T t) {
        return new ObjectMapper().writeValueAsBytes(t);
    }

}
