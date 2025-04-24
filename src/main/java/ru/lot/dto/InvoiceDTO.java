package ru.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.lot.enums.InvoiceStatus;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private Long id;
    private Long drawId;
    private String[] numbers;
    private Instant registerTime;
    private InvoiceStatus status;
}
