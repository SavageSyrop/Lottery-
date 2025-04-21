package ru.lot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lot.dao.InvoiceDao;
import ru.lot.dao.PaymentDao;
import ru.lot.entity.Invoice;
import ru.lot.entity.Payment;
import ru.lot.entity.User;

@Service
public class PaymentServiceImpl implements PaymentService {
    private InvoiceDao invoiceDao;
    private PaymentDao paymentDao;

    @Autowired
    public PaymentServiceImpl(InvoiceDao invoiceDao, PaymentDao paymentDao) {
        this.invoiceDao = invoiceDao;
        this.paymentDao = paymentDao;
    }

    @Override
    public Payment createPayment(User currentUser, Long invoiceId, Long cvc, String cardNumber, Double amount) {
        return null;
    }

    @Override
    public Invoice createInvoice(User currentUser, Long ticketId) {

        return null;
    }
}
