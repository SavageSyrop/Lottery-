package ru.lot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.lot.enums.DrawStatus;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "draws")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Draw extends AbstractEntity<Long> implements Identifiable<Long> {

    // TODO возможно придется переделать в енум
    private String lotteryType;

    // TODO аннотация для конвертации в БД
    private LocalDateTime startTime;

    @Column
    @Enumerated(EnumType.STRING)
    private DrawStatus status;

    public Draw(long id, String lotteryType, LocalDateTime startTime, DrawStatus drawStatus) {
        this.id = id;
        this.lotteryType = lotteryType;
        this.startTime = startTime;
        this.status = drawStatus;
    }

    @Override
    public void setId(Long index) {
        this.id = index;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}