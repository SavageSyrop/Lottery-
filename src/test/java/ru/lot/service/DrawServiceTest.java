package ru.lot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.lot.dao.DrawRepositoryDoa;
import ru.lot.dao.DrawResultRepositoryDao;
import ru.lot.entity.Draw;
import ru.lot.entity.DrawResult;
import ru.lot.enums.DrawStatus;
import ru.lot.enums.LotteryType;
import ru.lot.service.DrawService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DrawServiceTest {

    @Mock
    private DrawRepositoryDoa drawRepository;

    @Mock
    private DrawResultRepositoryDao drawResultRepository;

    @InjectMocks
    private DrawService drawService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateDraw() {
        LotteryType lotteryType = LotteryType.FIVE_OUT_OF_36;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        Draw savedDraw = new Draw(1L, lotteryType, startTime, DrawStatus.PLANNED);

        when(drawRepository.save(any(Draw.class))).thenReturn(savedDraw);

        Draw result = drawService.createDraw(lotteryType.getLabel(), startTime);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(DrawStatus.PLANNED, result.getStatus());
        verify(drawRepository, times(1)).save(any(Draw.class));
    }

    @Test
    public void testGetActiveDraws() {
        Draw activeDraw1 = new Draw(1L, LotteryType.FIVE_OUT_OF_36, LocalDateTime.now().plusHours(1), DrawStatus.ACTIVE);
        Draw activeDraw2 = new Draw(2L, LotteryType.SIX_OUT_OF_45, LocalDateTime.now().plusHours(2), DrawStatus.ACTIVE);
        List<Draw> activeDraws = Arrays.asList(activeDraw1, activeDraw2);
        when(drawRepository.findByStatus(DrawStatus.ACTIVE)).thenReturn(activeDraws);

        List<Draw> result = drawService.getActiveDraws();

        assertEquals(2, result.size());
        verify(drawRepository, times(1)).findByStatus(DrawStatus.ACTIVE);
    }

    @Test
    public void testCancelDraw_Success() {
        Draw draw = new Draw(1L, LotteryType.FIVE_OUT_OF_36, LocalDateTime.now().plusHours(1), DrawStatus.PLANNED);
        when(drawRepository.findById(1L)).thenReturn(Optional.of(draw));
        when(drawRepository.save(draw)).thenReturn(draw);

        Draw cancelledDraw = drawService.cancelDraw(1L);

        assertEquals(DrawStatus.CANCELLED, cancelledDraw.getStatus());
        verify(drawRepository, times(1)).findById(1L);
        verify(drawRepository, times(1)).save(draw);
    }

    @Test
    public void testCancelDraw_NotFound() {
        when(drawRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> drawService.cancelDraw(1L));
        assertEquals("Тираж не найден", exception.getMessage());
        verify(drawRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCompletedDrawHistory() {
        Draw completedDraw = new Draw(1L, LotteryType.FIVE_OUT_OF_36, LocalDateTime.now().minusDays(1), DrawStatus.COMPLETED);
        List<Draw> completedDraws = List.of(completedDraw);
        when(drawRepository.findByStatus(DrawStatus.COMPLETED)).thenReturn(completedDraws);

        List<Draw> result = drawService.getCompletedDrawHistory();

        assertEquals(1, result.size());
        assertEquals(DrawStatus.COMPLETED, result.getFirst().getStatus());
        verify(drawRepository, times(1)).findByStatus(DrawStatus.COMPLETED);
    }

    @Test
    public void testGetDrawResult_Success() {
        Draw draw = new Draw(1L, LotteryType.FIVE_OUT_OF_36, LocalDateTime.now().plusHours(1), DrawStatus.COMPLETED);
        DrawResult expectedResult = new DrawResult(1L, draw, "1,2,3,4,5", LocalDateTime.now());
        when(drawResultRepository.findByDrawId(1L)).thenReturn(expectedResult);

        DrawResult result = drawService.getDrawResult(1L);

        assertNotNull(result);
        assertEquals("1,2,3,4,5", result.getWinningCombination());
        verify(drawResultRepository, times(1)).findByDrawId(1L);
    }

    @Test
    public void testGetDrawResult_NotFound() {
        when(drawResultRepository.findByDrawId(1L)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> drawService.getDrawResult(1L));
        assertEquals("Результат для тиража не найден", exception.getMessage());
        verify(drawResultRepository, times(1)).findByDrawId(1L);
    }
}
