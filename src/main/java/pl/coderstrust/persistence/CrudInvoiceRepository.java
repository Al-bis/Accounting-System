package pl.coderstrust.persistence;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

interface CrudInvoiceRepository extends CrudRepository<Invoice, Long> {

    List<Invoice> findByDateBetween(LocalDate fromDate, LocalDate toDate);

}
