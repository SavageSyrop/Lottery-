package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lot.entity.Ticket;
import ru.lot.enums.TicketStatus;

import java.util.List;

public interface TicketDao extends JpaRepository<Ticket, Long> {
    List<Ticket> findByDrawId(Long drawId);

    List<Ticket> findByDrawIdAndStatus(Long drawId, TicketStatus ticketStatus);
}
