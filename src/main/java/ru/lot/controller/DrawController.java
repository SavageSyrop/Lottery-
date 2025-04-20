package ru.lot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.lot.entity.DrawResult;
import ru.lot.service.DrawService;
import ru.lot.entity.Draw;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

//TODO сделать юнит тесты
@RestController
@RequestMapping("/api")
public class DrawController {

    private final DrawService drawService;

    @Autowired
    public DrawController(DrawService drawService) {
        this.drawService = drawService;
    }

    @PostMapping("/admin/draws")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Draw createDraw(@RequestBody Map<String, String> request) {
        String lotteryType = request.get("lotteryType");
        String startTimeStr = request.get("startTime");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
        return drawService.createDraw(lotteryType, startTime);
    }

    @GetMapping("/draws/active")
    public List<Draw> getActiveDraws() {
        return drawService.getActiveDraws();
    }

    @PutMapping("/admin/draws/{id}/cancel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Draw cancelDraw(@PathVariable("id") Long drawId) {
        return drawService.cancelDraw(drawId);
    }

    @GetMapping("/draws/history")
    public List<Draw> getCompletedDrawHistory() {
        return drawService.getCompletedDrawHistory();
    }

    @GetMapping("/draws/{id}/results")
    public DrawResult getDrawResult(@PathVariable("id") Long drawId) {
        return drawService.getDrawResult(drawId);
    }
}