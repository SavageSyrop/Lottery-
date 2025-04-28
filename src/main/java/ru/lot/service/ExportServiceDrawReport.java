package ru.lot.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.lot.entity.LotteryType;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportServiceDrawReport {
    private LotteryType lotteryType;

    private Instant startTime;

    private Instant resultTime;

    private String winningCombination;

    private List<Long> winningTickets;
}
