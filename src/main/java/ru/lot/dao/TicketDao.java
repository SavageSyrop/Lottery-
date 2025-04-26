package ru.lot.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lot.entity.Ticket;
import ru.lot.enums.TicketStatus;

import java.util.List;

public interface TicketDao extends JpaRepository<Ticket, Long> {
        List<Ticket> findByDrawIdAndStatus(Long drawId, TicketStatus ticketStatus);
        List<Ticket> findByUserId(Long userId);
        List<Ticket> findByUserIdAndDrawId(Long userId, Long drawId);
        List<Ticket> findByDrawId(Long drawId);
        Optional<Ticket> findByIdAndUserId(Long id, Long userId);
}
