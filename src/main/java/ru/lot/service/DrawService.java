package ru.lot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lot.entity.Draw;
import ru.lot.entity.DrawResult;
import ru.lot.enums.DrawStatus;
import ru.lot.dao.DrawRepository;
import ru.lot.dao.DrawResultRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DrawService {

    private final DrawRepository drawRepository;
    private final DrawResultRepository drawResultRepository;

    @Autowired
    public DrawService(DrawRepository drawRepository, DrawResultRepository drawResultRepository) {
        this.drawRepository = drawRepository;
        this.drawResultRepository = drawResultRepository;
    }

    @Transactional
    public Draw createDraw(String lotteryType, LocalDateTime startTime) {
        Draw draw = new Draw();
        draw.setLotteryType(lotteryType);
        draw.setStartTime(startTime);
        draw.setStatus(DrawStatus.PLANNED);
        return drawRepository.save(draw);
    }

    public List<Draw> getActiveDraws() {
        return drawRepository.findByStatus(DrawStatus.ACTIVE);
    }

    @Transactional
    public Draw cancelDraw(Long drawId) {
        Optional<Draw> drawOptional = drawRepository.findById(drawId);
        if (drawOptional.isEmpty()) {
            throw new RuntimeException("Тираж не найден");
        }

        Draw draw = drawOptional.get();
        draw.setStatus(DrawStatus.CANCELLED);
        //TODO Добавить логику поиска связанных тикетов и отмены их, либо ссылаться вызывать подобую логику в сервисе тикетов
        return drawRepository.save(draw);
    }

    public List<Draw> getCompletedDrawHistory() {
        return drawRepository.findByStatus(DrawStatus.COMPLETED);
    }

    public DrawResult getDrawResult(Long drawId) {
        DrawResult result = drawResultRepository.findByDrawId(drawId);
        if (result == null) {
            throw new RuntimeException("Результат для тиража не найден");
        }
        return result;
    }
}

