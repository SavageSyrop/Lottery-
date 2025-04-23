package ru.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.lot.enums.DrawStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrawDTO {
    private Long id;
    private String lotteryType;
    private LocalDateTime startTime;
    private DrawStatus status;
}
