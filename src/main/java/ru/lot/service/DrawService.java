package ru.lot.service;

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

@Service
public class DrawService {

    private final DrawDao drawRepository;
    private final DrawResultRepositoryDao drawResultRepository;
    private final LotteryTypeDao lotteryTypeDao;

    @Autowired
    public DrawService(DrawDao drawRepository, DrawResultRepositoryDao drawResultRepository, LotteryTypeDao lotteryTypeDao) {
        this.drawRepository = drawRepository;
        this.drawResultRepository = drawResultRepository;
        this.lotteryTypeDao = lotteryTypeDao;
    }

    @Transactional
    public Draw createDraw(String lottery, LocalDateTime startTime) {
        LotteryName lotteryName = LotteryName.fromLabel(lottery);
        LotteryType lotteryType = lotteryTypeDao.findByName(lotteryName).orElseThrow();
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

