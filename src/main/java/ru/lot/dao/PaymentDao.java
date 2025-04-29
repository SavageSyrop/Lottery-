package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lot.entity.Payment;

public interface PaymentDao extends JpaRepository<Payment, Long> {
}
