package pl.coderstrust.persistatnce;

import pl.coderstrust.domain.Invoice;

import java.util.Collection;

public interface InvoiceRepository {

    Collection<Invoice> getAllInvoices();

    void saveInvoice(Invoice invoice);

    Invoice getInvoice(Long id);

    void deleteInvoice(Long id);

}
