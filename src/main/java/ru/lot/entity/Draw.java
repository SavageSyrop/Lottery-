package ru.lot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.lot.converter.LocalDateTimeAttributeConverter;
import ru.lot.enums.DrawStatus;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "draws")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Draw extends AbstractEntity<Long> implements Identifiable<Long> {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lottery_type_id")
    private LotteryType lotteryType;

    @Column(name = "start_time", nullable = false)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime startTime;

    @Column
    @Enumerated(EnumType.STRING)
    private DrawStatus status;

    public Draw(long id, LotteryType lotteryType, LocalDateTime startTime, DrawStatus drawStatus) {
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