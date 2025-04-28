package ru.lot.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.lot.converter.TicketConverter;
import ru.lot.dto.DrawDTO;
import ru.lot.dto.TicketDTO;
import ru.lot.dto.TicketWithDrawDTO;
import ru.lot.dto.UserDTO;
import ru.lot.entity.Ticket;
import ru.lot.entity.User;
import ru.lot.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final TicketConverter ticketConverter;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public TicketDTO getTicket(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        Ticket ticket = ticketService.getTicketForUser(id, userId);
        return convertToDTO(ticket);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public List<TicketWithDrawDTO> getUserTickets(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        List<Ticket> tickets = ticketService.getUserTickets(userId);
        return tickets.stream()
                .map(this::convertToWithDrawDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDTO createTicket(
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        Long drawId = Long.parseLong(request.get("drawId").toString());
        
        @SuppressWarnings("unchecked")
        List<Integer> numbers = (List<Integer>) request.get("numbers");
        
        Ticket ticket = ticketService.createTicket(userId, drawId, numbers);
        return convertToDTO(ticket);
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = modelMapper.map(ticket, TicketDTO.class);
        dto.setUserId(ticket.getUser().getId());
        dto.setDrawId(ticket.getDraw().getId());
        dto.setNumbers(ticketConverter.convertToNumbers(ticket.getPickedNumbers()));
        return dto;
    }

    private TicketWithDrawDTO convertToWithDrawDTO(Ticket ticket) {
        TicketWithDrawDTO dto = modelMapper.map(ticket, TicketWithDrawDTO.class);
        dto.setNumbers(ticketConverter.convertToNumbers(ticket.getPickedNumbers()));
        return dto;
    }
}