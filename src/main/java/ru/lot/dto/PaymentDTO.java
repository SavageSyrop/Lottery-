package ru.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.lot.enums.PaymentStatus;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long invoiceId;
    private Double amount;
    private PaymentStatus status;
    private Instant registerTime;
}
