package ru.lot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lot.dao.DrawDao;
import ru.lot.dao.DrawResultRepositoryDao;
import ru.lot.dao.LotteryTypeDao;
import ru.lot.dao.TicketDao;
import ru.lot.entity.Draw;
import ru.lot.entity.DrawResult;
import ru.lot.entity.LotteryType;
import ru.lot.entity.Ticket;
import ru.lot.enums.DrawStatus;
import ru.lot.enums.LotteryName;
import ru.lot.enums.TicketStatus;
import ru.lot.event.DrawCreatedEvent;
import ru.lot.event.DrawEndedEvent;
import ru.lot.event.DrawStartedEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrawService {

    private final DrawDao drawRepository;
    private final DrawResultRepositoryDao drawResultRepository;
    private final LotteryTypeDao lotteryTypeDao;
    private final DrawResultService drawResultService;
    private final ApplicationEventPublisher eventPublisher;
    private final TicketDao ticketRepository;

    @Transactional
    public Draw createDraw(String lottery, LocalDateTime startTime) {
        log.info("Creating draw for lottery {} at {}", lottery, startTime);

        LotteryName lotteryName = LotteryName.fromLabel(lottery);
        LotteryType lotteryType = lotteryTypeDao.findByName(lotteryName)
                .orElseThrow(() -> {
                    log.error("Lottery type not found: {}", lotteryName);
                    return new RuntimeException("Lottery type not found");
                });

        Draw draw = new Draw();
        draw.setLotteryType(lotteryType);
        draw.setStartTime(startTime);
        draw.setStatus(DrawStatus.PLANNED);

        Draw savedDraw = drawRepository.save(draw);

        eventPublisher.publishEvent(new DrawCreatedEvent(savedDraw));

        log.info("Successfully created draw with id: {}", savedDraw.getId());

        return savedDraw;
    }

    @Transactional
    public void cancelTicketsForDraw(Long drawId) {
        List<Ticket> tickets = ticketRepository.findByDrawId(drawId);

        for (Ticket ticket : tickets) {
            ticket.setStatus(TicketStatus.LOSE);
        }

        ticketRepository.saveAll(tickets);
        log.info("Отменено {} билетов для тиража {}", tickets.size(), drawId);
    }

    @Transactional
    @EventListener
    public void handleDrawStarted(DrawStartedEvent event) {
        Long drawId = event.getDrawId();
        log.info("Handling DrawStartedEvent for draw id {}", drawId);

        Draw draw = drawRepository.findById(drawId)
                .orElseThrow(() -> {
                    log.error("Draw not found with id: {}", drawId);
                    return new RuntimeException("Тираж не найден");
                });

        draw.setStatus(DrawStatus.ACTIVE);
        drawRepository.save(draw);

        log.info("Draw {} is now ACTIVE", drawId);
    }

    @Transactional
    @EventListener
    public void handleDrawEnded(DrawEndedEvent event) {
        Long drawId = event.getDrawId();
        log.info("Handling DrawEndedEvent for draw id {}", drawId);

        Draw draw = drawRepository.findById(drawId)
                .orElseThrow(() -> {
                    log.error("Draw not found with id: {}", drawId);
                    return new RuntimeException("Тираж не найден");
                });

        draw.setStatus(DrawStatus.COMPLETED);
        drawRepository.save(draw);

        log.info("Draw {} is now COMPLETED", drawId);

        int[] winningCombination = drawResultService.getWinningCombination(draw.getLotteryType().getName());
        String winningCombinationStr = drawResultService.winningCombinationToString(winningCombination, ",");

        drawResultRepository.save(new DrawResult(draw, winningCombinationStr, LocalDateTime.now()));

        drawResultService.markTicketsWinOrLose(drawId, winningCombinationStr);

        log.info("Draw {} finished with winning combination {}", drawId, winningCombinationStr);
    }

    public List<Draw> getActiveDraws() {
        log.info("Fetching active draws");
        List<Draw> activeDraws = drawRepository.findByStatus(DrawStatus.ACTIVE);
        log.debug("Found {} active draws", activeDraws.size());
        return activeDraws;
    }

    @Transactional
    public Draw cancelDraw(Long drawId) {
        log.info("Cancelling draw with id: {}", drawId);

        Optional<Draw> drawOptional = drawRepository.findById(drawId);
        if (drawOptional.isEmpty()) {
            log.error("Draw not found with id: {}", drawId);
            throw new RuntimeException("Тираж не найден");
        }

        Draw draw = drawOptional.get();
        draw.setStatus(DrawStatus.CANCELLED);
        Draw savedDraw = drawRepository.save(draw);

        cancelTicketsForDraw(drawId);

        log.info("Successfully cancelled draw and related tickets for draw id: {}", drawId);

        return savedDraw;
    }

    public List<Draw> getCompletedDrawHistory() {
        log.info("Fetching completed draw history");
        List<Draw> completedDraws = drawRepository.findByStatus(DrawStatus.COMPLETED);
        log.debug("Found {} completed draws", completedDraws.size());
        return completedDraws;
    }

    public DrawResult getDrawResult(Long drawId) {
        log.info("Fetching draw result for draw id: {}", drawId);

        DrawResult result = drawResultRepository.findByDrawId(drawId);
        if (result == null) {
            log.error("Draw result not found for draw id: {}", drawId);
            throw new RuntimeException("Результат для тиража не найден");
        }

        log.debug("Found draw result for draw id: {}", drawId);
        return result;
    }

    public Optional<Draw> getDrawById(Long drawId) {
        log.info("Fetching draw by id: {}", drawId);
        return drawRepository.findById(drawId);
    }
}
