package pl.coderstrust.persistence;

import java.time.LocalDate;
import java.util.Collection;

public interface InvoiceRepository {

    Collection<Invoice> getAllInvoices();

    Collection<Invoice> getInvoices(LocalDate fromDate, LocalDate toDate);

    Long saveInvoice(Invoice invoice);

    Invoice getInvoice(Long id);

    void deleteInvoice(Long id);

}
