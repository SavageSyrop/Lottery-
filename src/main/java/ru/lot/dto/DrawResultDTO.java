package ru.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrawResultDTO {
    private Long id;
    private Long drawId;
    private String winningCombination;
    private Instant resultTime;
}
