package ru.lot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.lot.enums.LotteryName.FIVE_OUT_OF_36;
import static ru.lot.enums.LotteryName.SIX_OUT_OF_45;

public class DrawServiceTest {

    @Mock
    private DrawDao drawRepository;

    @Mock
    private DrawResultRepositoryDao drawResultRepository;

    @Mock
    private LotteryTypeDao lotteryTypeDao;

    @Mock
    private DrawResultService drawResultService;

    @Mock
    private TicketDao ticketRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DrawService drawService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        LotteryType typeFiveToThirtySix = new LotteryType(FIVE_OUT_OF_36, "Desc", false, 500d, 500000d);
        LotteryType typeSixToFortyFive = new LotteryType(SIX_OUT_OF_45, "Desc", false, 500d, 500000d);
        when(lotteryTypeDao.findByName(FIVE_OUT_OF_36)).thenReturn(Optional.of(typeFiveToThirtySix));
        when(lotteryTypeDao.findByName(SIX_OUT_OF_45)).thenReturn(Optional.of(typeSixToFortyFive));
    }

    @Test
    public void testCreateDraw() {
        LotteryType lotteryType = new LotteryType();
        lotteryType.setName(FIVE_OUT_OF_36);

        when(lotteryTypeDao.findByName(FIVE_OUT_OF_36)).thenReturn(Optional.of(lotteryType));

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        Draw savedDraw = new Draw();
        savedDraw.setId(1L);
        savedDraw.setLotteryType(lotteryType);
        savedDraw.setStartTime(startTime);
        savedDraw.setStatus(DrawStatus.PLANNED);

        when(drawRepository.save(any(Draw.class))).thenReturn(savedDraw);

        Draw result = drawService.createDraw(FIVE_OUT_OF_36.getLabel(), startTime);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(DrawStatus.PLANNED, result.getStatus());

        verify(drawRepository, times(1)).save(any(Draw.class));
    }

    @Test
    public void testGetActiveDraws() {
        LotteryType lotteryOne = lotteryTypeDao.findByName(FIVE_OUT_OF_36).orElseThrow();
        LotteryType lotteryTwo = lotteryTypeDao.findByName(SIX_OUT_OF_45).orElseThrow();
        Draw activeDraw1 = new Draw(1L, lotteryOne, LocalDateTime.now().plusHours(1), DrawStatus.ACTIVE);
        Draw activeDraw2 = new Draw(2L, lotteryTwo, LocalDateTime.now().plusHours(2), DrawStatus.ACTIVE);
        List<Draw> activeDraws = Arrays.asList(activeDraw1, activeDraw2);
        when(drawRepository.findByStatus(DrawStatus.ACTIVE)).thenReturn(activeDraws);

        List<Draw> result = drawService.getActiveDraws();

        assertEquals(2, result.size());
        verify(drawRepository, times(1)).findByStatus(DrawStatus.ACTIVE);
    }

    @Test
    public void testCancelDraw_Success() {
        LotteryType lotteryOne = lotteryTypeDao.findByName(FIVE_OUT_OF_36).orElseThrow();
        Draw draw = new Draw(1L, lotteryOne, LocalDateTime.now().plusHours(1), DrawStatus.PLANNED);

        when(drawRepository.findById(1L)).thenReturn(Optional.of(draw));
        when(drawRepository.save(draw)).thenReturn(draw);

        Draw cancelledDraw = drawService.cancelDraw(1L);

        assertEquals(DrawStatus.CANCELLED, cancelledDraw.getStatus());
        verify(drawRepository, times(1)).findById(1L);
        verify(drawRepository, times(1)).save(draw);
        verify(ticketRepository, times(1)).findByDrawId(1L);
        verify(ticketRepository, times(1)).saveAll(anyList());
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
        LotteryType lotteryOne = lotteryTypeDao.findByName(FIVE_OUT_OF_36).orElseThrow();
        Draw completedDraw = new Draw(1L, lotteryOne, LocalDateTime.now().minusDays(1), DrawStatus.COMPLETED);
        List<Draw> completedDraws = List.of(completedDraw);
        when(drawRepository.findByStatus(DrawStatus.COMPLETED)).thenReturn(completedDraws);

        List<Draw> result = drawService.getCompletedDrawHistory();

        assertEquals(1, result.size());
        assertEquals(DrawStatus.COMPLETED, result.getFirst().getStatus());
        verify(drawRepository, times(1)).findByStatus(DrawStatus.COMPLETED);
    }

    @Test
    public void testGetDrawResult_Success() {
        LotteryType lotteryOne = lotteryTypeDao.findByName(FIVE_OUT_OF_36).orElseThrow();
        Draw draw = new Draw(1L, lotteryOne, LocalDateTime.now().plusHours(1), DrawStatus.COMPLETED);
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

    @Test
    public void testCancelTicketsForDraw() {
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        List<Ticket> tickets = Arrays.asList(ticket1, ticket2);

        when(ticketRepository.findByDrawId(1L)).thenReturn(tickets);

        drawService.cancelTicketsForDraw(1L);

        assertEquals(TicketStatus.LOSE, ticket1.getStatus());
        assertEquals(TicketStatus.LOSE, ticket2.getStatus());
        verify(ticketRepository, times(1)).findByDrawId(1L);
        verify(ticketRepository, times(1)).saveAll(tickets);
    }
}
