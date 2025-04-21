package ru.lot.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.lot.dto.InvoiceDTO;
import ru.lot.dto.PaymentDTO;
import ru.lot.entity.Invoice;
import ru.lot.entity.Payment;
import ru.lot.entity.User;
import ru.lot.service.PaymentService;
import ru.lot.service.UserService;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private UserService userService;
    private PaymentService paymentService;

    private ModelMapper modelMapper;

    @Autowired
    public PaymentController(UserService userService, PaymentService paymentService, ModelMapper modelMapper) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/payments")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public PaymentDTO createPayment(@RequestParam Long invoiceId, @RequestParam Long cvc, @RequestParam String cardNumber, @RequestParam Double amount) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Payment payment = this.paymentService.createPayment(currentUser, invoiceId, cvc, cardNumber, amount);
        PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);
        return paymentDTO;
    }

    @PostMapping("/invoice")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public InvoiceDTO createInvoice(@RequestParam Long ticketId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Invoice invoice = this.paymentService.createInvoice(currentUser, ticketId);
        InvoiceDTO invoiceDTO = modelMapper.map(invoice, InvoiceDTO.class);
        return invoiceDTO;

    }
}
