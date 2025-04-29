package ru.lot.event;

import lombok.Getter;
import ru.lot.entity.Draw;

@Getter
public class DrawCreatedEvent {
    private final Draw draw;

    public DrawCreatedEvent(Draw draw) {
        this.draw = draw;
    }
}
