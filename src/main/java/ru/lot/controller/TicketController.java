package ru.lot.controller;

import ru.lot.dto.TicketResponse;
import ru.lot.model.Ticket;
import ru.lot.repository.TicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getTicketById(@PathVariable Long id, @RequestHeader Long userId) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    if (!ticket.getUserId().equals(userId)) {
                        return ResponseEntity.status(403).build();
                    }
                    return ResponseEntity.ok(convertToResponse(ticket));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<TicketResponse> getUserTickets(@RequestHeader Long userId) {
        return ticketRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private TicketResponse convertToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setUserId(ticket.getUserId());
        response.setDrawId(ticket.getDrawId());
        response.setNumbers(ticket.getNumbers());
        response.setStatus(ticket.getStatus());
        
        // Здесь должна быть логика получения информации о тираже (drawInfo)
        // Для примера оставлено пустым
        TicketResponse.DrawInfo drawInfo = new TicketResponse.DrawInfo();
        drawInfo.setId(ticket.getDrawId());
        response.setDrawInfo(drawInfo);
        
        return response;
    }
}