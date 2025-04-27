package ru.lot.event;

import lombok.Getter;

@Getter
public class DrawStartedEvent {
    private final Long drawId;

    public DrawStartedEvent(Long drawId) {
        this.drawId = drawId;
    }
}
