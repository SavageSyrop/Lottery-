package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lot.entity.Ticket;

public interface TicketDao extends JpaRepository<Ticket, Long> {
}
