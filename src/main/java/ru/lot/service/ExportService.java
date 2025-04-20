package ru.lot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportService {
    private DrawService drawService;

    // TODO TicketService

    @Autowired
    public ExportService(DrawService drawService) {
        this.drawService = drawService;
    }

    public void getWinningTicketsInfoAsJson() {

    }

    public void getWinningTicketsInfoAsCSV() {

    }
}
