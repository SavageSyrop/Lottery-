package ru.lot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import ru.lot.entity.Draw;
import ru.lot.event.DrawCreatedEvent;
import ru.lot.event.DrawEndedEvent;
import ru.lot.event.DrawStartedEvent;

import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrawTaskSchedulerService {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final ApplicationEventPublisher eventPublisher; // теперь здесь EventPublisher

    @EventListener
    public void handleDrawCreated(DrawCreatedEvent event) {
        Draw draw = event.getDraw();
        log.info("Received DrawCreatedEvent for draw id {}", draw.getId());
        scheduleStartTask(draw);
        scheduleEndTask(draw);
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

