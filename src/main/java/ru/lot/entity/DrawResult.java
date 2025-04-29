package ru.lot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
    private Instant resultTime;

    public DrawResult(Draw draw, String winningCombination, Instant resultTime) {
        this.draw = draw;
        this.winningCombination = winningCombination;
        this.resultTime = resultTime;
    }

}
