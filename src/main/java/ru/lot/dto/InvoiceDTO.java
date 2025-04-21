package ru.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.lot.enums.InvoiceStatus;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private Long id;
    private Long ticketId;
    private Instant registerTime;
    private InvoiceStatus status;
}
