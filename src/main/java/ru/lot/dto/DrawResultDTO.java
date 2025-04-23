package ru.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrawResultDTO {
    private Long id;
    private Long drawId;
    private String winningCombination;
    private LocalDateTime resultTime;
}
