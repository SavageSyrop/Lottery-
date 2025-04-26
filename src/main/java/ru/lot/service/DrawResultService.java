package ru.lot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.lot.dao.TicketDao;
import ru.lot.entity.LotteryType;
import ru.lot.entity.Ticket;
import ru.lot.enums.LotteryName;
import ru.lot.enums.TicketStatus;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class DrawResultService {
    private SecureRandom secureRandom;

    private TicketDao ticketRepository;

    @Value("{draw_result_service.secure_random.algorithm}")
    private String secureRandomAlgorithm = "NativePRNG";

    @Autowired
    public DrawResultService(TicketDao ticketRepository) throws NoSuchAlgorithmException {
        this.secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
        this.ticketRepository = ticketRepository;
    }

    public boolean isCombinationWinning(String combination, String winningCombination) {
        return combination.compareTo(winningCombination) == 0;
    }

    public boolean isCombinationWinning(
            int[] combination,
            int[] winningCombination) {
        if (combination.length != winningCombination.length) return false;
        for (int i = 0; i < combination.length; i++) {
            if (combination[i] != winningCombination[i]) return false;
        }
        return true;
    }

    public String winningCombinationToString(
            int[] winningCombination,
            String delimeter) {
        StringBuilder winningCombinationString = new StringBuilder();
        for (int i = 0; i < winningCombination.length - 1; i++) {
            int number = winningCombination[i];
            winningCombinationString.append(number);
            winningCombinationString.append(delimeter);
        }
        winningCombinationString.append(winningCombination[winningCombination.length - 1]);
        return winningCombinationString.toString();
    }

    public int[] getWinningCombination(LotteryType lotteryType) {
        return getWinningCombination(lotteryType.getName());
    }

    public int[] getWinningCombination(LotteryName lotteryName) {
        switch (lotteryName) {
            case SIX_OUT_OF_45 -> {
                return getWinningCombination(45, 1, 46);
            }
            case FIVE_OUT_OF_36 -> {
                return getWinningCombination(36, 1, 37);
            }
            case null, default -> throw new RuntimeException();
        }
    }

    public int[] getWinningCombination(
            int combinationLength,
            int lowerBound,
            int upperBound) {
        return secureRandom.ints(combinationLength, lowerBound, upperBound).toArray();
    }

    public void markTicketsWinOrLose(
            Long drawId,
            String winningCombination
    ) {
        List<Ticket> tickets = ticketRepository.findByDrawId(drawId);
        tickets.forEach(ticket -> {
            if (isCombinationWinning(ticket.getPickedNumbers(), winningCombination)) {
                ticket.setStatus(TicketStatus.WIN);
            } else {
                ticket.setStatus(TicketStatus.LOSE);
            }
        });
        ticketRepository.saveAll(tickets);
    }
}
