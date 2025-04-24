package ru.lot.service;

import ru.lot.entity.Invoice;
import ru.lot.entity.Payment;
import ru.lot.entity.User;

public interface PaymentService {
    Payment createPayment(User currentUser, Long invoiceId, Long cvc, String cardNumber, Double amount);

    Invoice createInvoice(User currentUser, String selectedNumbers, Long drawId);
}
