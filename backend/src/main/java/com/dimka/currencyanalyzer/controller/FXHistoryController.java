package com.dimka.currencyanalyzer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FXHistoryController {

    @PostMapping("/history/zip")
    public void applyHistory() {

    }

    @GetMapping("/history/zip")
    public byte[] downloadHistory() {
        return null;
    }
}
