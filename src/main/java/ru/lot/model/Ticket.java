package ru.lot.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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