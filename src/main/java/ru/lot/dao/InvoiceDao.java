package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lot.entity.Invoice;

public interface InvoiceDao extends JpaRepository<Invoice, Long> {
}
