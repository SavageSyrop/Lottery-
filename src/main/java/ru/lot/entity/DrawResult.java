package ru.lot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.lot.converter.LocalDateTimeAttributeConverter;
import ru.lot.converter.WinningCombinationConverter;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "draw_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawResult extends AbstractEntity<Long> implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "draw_id", referencedColumnName = "id")
    private Draw draw;

    @Column(name = "winning_combination", nullable = false)
    private String winningCombination;

    @Column(name = "result_time", nullable = false)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime resultTime;

    public DrawResult(Draw draw, String winningCombination, LocalDateTime resultTime) {
        this.draw = draw;
        this.winningCombination = winningCombination;
        this.resultTime = resultTime;
    }

}
