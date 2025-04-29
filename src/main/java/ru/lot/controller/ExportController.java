package ru.lot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.lot.service.ExportService;

@RestController
@RequestMapping("/api")
public class ExportController {
    private final ExportService exportService;

    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/draws/{id}/results/csv")
    public String getDrawResultCsv(@PathVariable("id") Long drawId) {
        return exportService.getWinningTicketsInfoAsCSVString(drawId);
    }

    @GetMapping("/draws/{id}/results/report")
    public String getDrawResultReport(@PathVariable("id") Long drawId) throws JsonProcessingException {
        return exportService.getDrawReportAsJson(drawId);
    }
}
