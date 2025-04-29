package ru.lot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.lot.enums.InvoiceStatus;

import java.time.Instant;


@Entity
@Table(name = "invoice")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Invoice extends AbstractEntity<Long> implements Identifiable<Long> {
    @Column
    private String pickedNumbers;
    @OneToOne
    @JoinColumn(name = "draw_id", referencedColumnName = "id")
    private Draw draw;
    @Column
    private Instant registerTime;
    @Enumerated(EnumType.STRING)
    @Column
    private InvoiceStatus status;
}
