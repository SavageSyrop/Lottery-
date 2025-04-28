package ru.lot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.stream.JsonWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lot.dao.DrawDao;
import ru.lot.dao.TicketDao;
import ru.lot.entity.Draw;
import ru.lot.entity.DrawResult;
import ru.lot.entity.Ticket;
import ru.lot.enums.TicketStatus;

import java.util.List;

@Service
public class ExportService {
    private DrawDao drawDao;

    private DrawService drawService;

    private TicketDao ticketDao;

    @Autowired
    public ExportService(DrawService drawService,
                         DrawDao drawDao,
                         TicketDao ticketDao
    ) {
        this.drawService = drawService;
        this.ticketDao = ticketDao;
        this.drawDao = drawDao;
    }

    public String getWinningTicketsInfoAsCSVString(Long drawId) {
        List<Ticket> winningTickets = ticketDao.findByDrawIdAndStatus(drawId, TicketStatus.WIN);
        StringBuilder csvStringBuilder = new StringBuilder();

        csvStringBuilder.append("Ticket Id, Status%n");

        winningTickets.forEach(ticket -> {
            csvStringBuilder.append(String.format("%s,%s%n", ticket.getId(), ticket.getStatus()));
        });

        return csvStringBuilder.toString();
    }

    public ExportServiceDrawReport getDrawReport(
            Long drawId
    ) {
        DrawResult drawResult = drawService.getDrawResult(drawId);
        Draw draw = drawDao.findById(drawId).get();
        ExportServiceDrawReport exportServiceDrawReport = new ExportServiceDrawReport();
        List<Ticket> winningTickets = ticketDao.findByDrawIdAndStatus(drawId, TicketStatus.WIN);

        exportServiceDrawReport.setStartTime(draw.getStartTime());
        exportServiceDrawReport.setResultTime(drawResult.getResultTime());
        exportServiceDrawReport.setLotteryType(draw.getLotteryType());
        exportServiceDrawReport.setWinningCombination(drawResult.getWinningCombination());
        exportServiceDrawReport.setWinningTickets(winningTickets.stream().map(Ticket::getId).toList());

        return exportServiceDrawReport;
    }

    public String getDrawReportAsJson(Long drawId) throws JsonProcessingException {
        ExportServiceDrawReport exportServiceDrawReport = getDrawReport(drawId);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(exportServiceDrawReport);
    }
}
