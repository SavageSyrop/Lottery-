package ru.lot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import ru.lot.entity.Draw;
import ru.lot.enums.DrawStatus;

import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrawTaskSchedulerService {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final DrawService drawService;

    public void scheduleDrawTasks(Draw draw) {
        scheduleStartTask(draw);
        scheduleEndTask(draw);
    }

    private void scheduleStartTask(Draw draw) {
        Date startDate = Date.from(draw.getStartTime().atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(() -> {
            log.info("Тираж {} стартует!", draw.getId());
            drawService.updateStatus(draw.getId(), DrawStatus.ACTIVE);
        }, startDate);
    }

    private void scheduleEndTask(Draw draw) {
        Date endDate = Date.from(draw.getEndTime().atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(() -> {
            log.info("Тираж {} завершается!", draw.getId());
            drawService.updateStatus(draw.getId(), DrawStatus.COMPLETED);
        }, endDate);
    }
}
