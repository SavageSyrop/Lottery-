package ru.lot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "draw_results")
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

    // TODO нужно пометить split-символ куда нибудь в проперти
    private String winningCombination;

    // TODO аннотация для конвертации в БД
    private LocalDateTime resultTime;
}
