package ru.lot.dto;

import ru.lot.model.Ticket;
import lombok.Data;

import java.util.List;

@Data
public class TicketResponse {
    private Long id;
    private Long userId;
    private Long drawId;
    private List<Integer> numbers;
    private Ticket.TicketStatus status;
    private DrawInfo drawInfo;

    @Data
    public static class DrawInfo {
        private Long id;
        private String type;
        private String status;
    }
}