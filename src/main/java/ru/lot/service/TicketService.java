package ru.lot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.lot.converter.TicketConverter;
import ru.lot.dao.TicketDao;
import ru.lot.dao.UserDao;
import ru.lot.entity.Draw;
import ru.lot.entity.Ticket;
import ru.lot.entity.User;
import ru.lot.enums.DrawStatus;
import ru.lot.enums.LotteryName;
import ru.lot.enums.TicketStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketDao ticketRepository;
    private final DrawService drawService;
    private final UserDao userRepository;
    private final TicketConverter ticketConverter;

    @Transactional
    public Ticket createTicket(Long userId, Long drawId, List<Integer> numbers) {
        log.info("Creating ticket for user {} and draw {}", userId, drawId);
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.error("User not found with id: {}", userId);
                        return new IllegalArgumentException("User not found");
                    });
            
            log.debug("Found user: {}", user.getId());

            Draw draw = drawService.getDrawById(drawId)
                    .orElseThrow(() -> {
                        log.error("Draw not found with id: {}", drawId);
                        return new IllegalArgumentException("Draw not found");
                    });

            log.debug("Found draw: {} with status: {}", draw.getId(), draw.getStatus());

            if (draw.getStatus() != DrawStatus.ACTIVE) {
                log.warn("Attempt to create ticket for inactive draw: {}", draw.getId());
                throw new IllegalArgumentException("Draw is not active");
            }

            log.debug("Validating numbers for lottery type: {}", draw.getLotteryType());
            validateNumbers(numbers, draw.getLotteryType().getName());

            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setDraw(draw);
            ticket.setPickedNumbers(ticketConverter.convertToString(numbers));
            ticket.setStatus(TicketStatus.PENDING);

            Ticket savedTicket = ticketRepository.save(ticket);
            log.info("Successfully created ticket with id: {}", savedTicket.getId());
            
            return savedTicket;
        } catch (Exception e) {
            log.error("Error creating ticket for user {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void cancelTicketsForDraw(Long drawId) {
        List<Ticket> tickets = ticketRepository.findByDrawId(drawId);

        for (Ticket ticket : tickets) {
            ticket.setStatus(TicketStatus.LOSE);
        }

        ticketRepository.saveAll(tickets);
        log.info("Отменено {} билетов для тиража {}", tickets.size(), drawId);
    }


    private void validateNumbers(List<Integer> numbers, LotteryName lotteryType) {
        log.debug("Validating numbers for lottery type: {}", lotteryType);
        
        switch (lotteryType) {
            case FIVE_OUT_OF_36:
                validateFiveOfThirtySix(numbers);
                break;
            case SIX_OUT_OF_45:
                validateSixOutOf45(numbers);
                break;
            default:
                log.error("Unsupported lottery type: {}", lotteryType);
                throw new IllegalArgumentException("Unsupported lottery type");
        }
    }

    private void validateFiveOfThirtySix(List<Integer> numbers) {
        log.debug("Validating 5/36 lottery numbers");
        
        if (numbers.size() != 5) {
            log.error("Invalid numbers count: {}, expected 5", numbers.size());
            throw new IllegalArgumentException("Exactly 5 numbers required for 5/36 lottery");
        }
        
        long uniqueCount = numbers.stream().distinct().count();
        if (uniqueCount != 5) {
            log.error("Numbers are not unique, unique count: {}", uniqueCount);
            throw new IllegalArgumentException("Numbers must be unique");
        }
        
        if (numbers.stream().anyMatch(n -> n < 1 || n > 36)) {
            log.error("Numbers out of range: {}", numbers);
            throw new IllegalArgumentException("Numbers must be between 1 and 36");
        }
        
        log.debug("Numbers validation passed successfully");
    }
 
    private void validateSixOutOf45(List<Integer> numbers) {
        log.debug("Validating 6/45 lottery numbers");
        
        if (numbers.size() != 6) {
            log.error("Invalid numbers count: {}, expected 6", numbers.size());
            throw new IllegalArgumentException("Exactly 6 numbers required for 6/45 lottery");
        }
        
        long uniqueCount = numbers.stream().distinct().count();
        if (uniqueCount != 6) {
            log.error("Numbers are not unique, unique count: {}", uniqueCount);
            throw new IllegalArgumentException("Numbers must be unique");
        }
        
        if (numbers.stream().anyMatch(n -> n < 1 || n > 45)) {
            log.error("Numbers out of range: {}", numbers);
            throw new IllegalArgumentException("Numbers must be between 1 and 45");
        }
        
        log.debug("Numbers validation passed successfully");
    }

    public Ticket getTicketForUser(Long ticketId, Long userId) {
        log.info("Getting ticket {} for user {}", ticketId, userId);
        
        return ticketRepository.findByIdAndUserId(ticketId, userId)
                .orElseThrow(() -> {
                    log.error("Ticket not found with id: {} for user: {}", ticketId, userId);
                    return new IllegalArgumentException("Ticket not found");
                });
    }

    public List<Ticket> getUserTickets(Long userId) {
        log.info("Getting all tickets for user {}", userId);
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        log.debug("Found {} tickets for user {}", tickets.size(), userId);
        return tickets;
    }

    public List<Ticket> getUserTicketsForDraw(Long userId, Long drawId) {
        log.info("Getting tickets for user {} and draw {}", userId, drawId);
        List<Ticket> tickets = ticketRepository.findByUserIdAndDrawId(userId, drawId);
        log.debug("Found {} tickets for user {} and draw {}", tickets.size(), userId, drawId);
        return tickets;
    }

    @Transactional
    public void updateTicketStatus(Long ticketId, TicketStatus status) {
        log.info("Updating status of ticket {} to {}", ticketId, status);
        
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> {
                        log.error("Ticket not found with id: {}", ticketId);
                        return new IllegalArgumentException("Ticket not found");
                    });

            ticket.setStatus(status);
            ticketRepository.save(ticket);
            log.info("Successfully updated status of ticket {}", ticketId);
        } catch (Exception e) {
            log.error("Error updating ticket {} status: {}", ticketId, e.getMessage(), e);
            throw e;
        }
    }
}