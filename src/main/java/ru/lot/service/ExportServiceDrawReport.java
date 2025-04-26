package ru.lot.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.lot.entity.LotteryType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportServiceDrawReport {
    private LotteryType lotteryType;

    private LocalDateTime startTime;

    private LocalDateTime resultTime;

    private String winningCombination;

    private List<Long> winningTickets;
}
