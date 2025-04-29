package ru.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.lot.enums.DrawStatus;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrawDTO {
    private Long id;
    private String lotteryType;
    private Instant startTime;
    private DrawStatus status;
}
