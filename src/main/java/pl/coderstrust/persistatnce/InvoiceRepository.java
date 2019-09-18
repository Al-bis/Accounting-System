package pl.coderstrust.persistatnce;

import pl.coderstrust.domain.Invoice;

import java.time.LocalDate;
import java.util.Collection;

public interface InvoiceRepository {

    Collection<Invoice> getAllInvoices();

    Collection<Invoice> getAllInvoices(LocalDate fromDate, LocalDate toDate);

    void saveInvoice(Invoice invoice);

    Invoice getInvoice(Long id);

    void deleteInvoice(Long id);

}
