package pl.coderstrust.database;

import pl.coderstrust.model.Invoice;

import java.util.Collection;
import java.util.Optional;

public interface Database {

    Collection<Invoice> getInvoices();

    void saveInvoice(Invoice invoice);

    void updateInvoice(Invoice invoice);

    Optional<Invoice> getInvoiceById(Long id);

    void deleteInvoiceById(Long id);
}
