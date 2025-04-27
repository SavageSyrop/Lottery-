package ru.lot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import ru.lot.dao.DrawDao;
import ru.lot.entity.Draw;
import ru.lot.enums.DrawStatus;
import ru.lot.event.DrawCreatedEvent;
import ru.lot.event.DrawEndedEvent;
import ru.lot.event.DrawStartedEvent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrawTaskSchedulerService {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final ApplicationEventPublisher eventPublisher;
    private final DrawDao drawDao;

    @EventListener
    public void handleDrawCreated(DrawCreatedEvent event) {
        Draw draw = event.getDraw();
        log.info("Received DrawCreatedEvent for draw id {}", draw.getId());
        scheduleStartTask(draw);
        scheduleEndTask(draw);
    }

    public void init() {
        var now = LocalDateTime.now();
        List<Draw> draws = drawDao.findByStatus(DrawStatus.PLANNED);
        for (Draw draw : draws) {
            if (draw.getStartTime().isAfter(now)) {
                scheduleStartTask(draw);
            }
            if (draw.getEndTime().isAfter(now)) {
                scheduleEndTask(draw);
            }
        }
    }

    private void scheduleStartTask(Draw draw) {
        Date startDate = Date.from(draw.getStartTime().atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(() -> {
            log.info("Тираж {} стартует!", draw.getId());
            eventPublisher.publishEvent(new DrawStartedEvent(draw.getId()));
        }, startDate);
    }

    private void scheduleEndTask(Draw draw) {
        Date endDate = Date.from(draw.getEndTime().atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(() -> {
            log.info("Тираж {} завершается!", draw.getId());
            eventPublisher.publishEvent(new DrawEndedEvent(draw.getId()));
        }, endDate);
    }
}
