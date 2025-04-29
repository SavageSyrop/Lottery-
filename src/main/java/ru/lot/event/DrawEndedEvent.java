package ru.lot.event;

import lombok.Getter;

@Getter
public class DrawEndedEvent {
    private final Long drawId;

    public DrawEndedEvent(Long drawId) {
        this.drawId = drawId;
    }
}
