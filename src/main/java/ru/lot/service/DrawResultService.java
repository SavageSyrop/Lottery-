package ru.lot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class DrawResultService {
    private SecureRandom secureRandom;

    // TODO TicketService

    @Value("{draw_result_service.secure_random.algorithm}")
    private String secureRandomAlgorithm = "NativePRNG";

    public DrawResultService() throws NoSuchAlgorithmException {
        this.secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
    }

    public String getWinningCombination() {
        return null;
    }

    public void markTicketsWinOrLose() {

    }
}
