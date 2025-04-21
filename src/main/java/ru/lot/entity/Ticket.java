package ru.lot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Ticket extends AbstractEntity<Long> implements Identifiable<Long> {

    private Long userId;
    private Long drawId;

    @ElementCollection
    private List<Integer> numbers;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    public enum TicketStatus {
        PENDING, WIN, LOSE
    }
}
