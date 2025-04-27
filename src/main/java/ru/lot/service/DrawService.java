package ru.lot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lot.dao.DrawDao;
import ru.lot.dao.DrawResultRepositoryDao;
import ru.lot.dao.LotteryTypeDao;
import ru.lot.entity.Draw;
import ru.lot.entity.DrawResult;
import ru.lot.entity.LotteryType;
import ru.lot.enums.DrawStatus;
import ru.lot.enums.LotteryName;

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
    private final DrawTaskSchedulerService drawTaskSchedulerService;
    private final DrawResultService drawResultService;
    private final TicketService ticketService;

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

        drawTaskSchedulerService.scheduleDrawTasks(savedDraw);

        log.info("Successfully created draw with id: {}", savedDraw.getId());

        return savedDraw;
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

        ticketService.cancelTicketsForDraw(drawId);

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

    @Transactional
    public void updateStatus(Long drawId, DrawStatus status) {
        log.info("Updating status of draw {} to {}", drawId, status);

        Draw draw = drawRepository.findById(drawId)
                .orElseThrow(() -> {
                    log.error("Draw not found with id: {}", drawId);
                    return new RuntimeException("Тираж не найден");
                });

        draw.setStatus(status);
        drawRepository.save(draw);

        if (status == DrawStatus.COMPLETED) {
            log.info("Processing draw completion for draw id: {}", drawId);

            int[] winningCombination = drawResultService.getWinningCombination(draw.getLotteryType().getName());
            String winningCombinationStr = drawResultService.winningCombinationToString(winningCombination, ",");

            drawResultRepository.save(new DrawResult(draw, winningCombinationStr, LocalDateTime.now()));

            drawResultService.markTicketsWinOrLose(drawId, winningCombinationStr);

            log.info("Draw {} completed with winning combination {}", drawId, winningCombinationStr);
        }
    }
}
