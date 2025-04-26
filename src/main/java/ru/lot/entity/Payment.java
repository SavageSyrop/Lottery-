package ru.lot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.lot.enums.PaymentStatus;

import java.time.Instant;

@Entity
@Table(name = "payment")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Payment extends AbstractEntity<Long> implements Identifiable<Long> {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
    @Column
    private Double amount;
    @Column
    private PaymentStatus status;
    @Column
    private Instant registerTime;
}
