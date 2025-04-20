package ru.lot.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import ru.lot.enums.TicketStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private Long userId;
    private Long drawId;
    private List<Integer> numbers;
    private TicketStatus status;
    private LocalDateTime createdAt;
}