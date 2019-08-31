package pl.coderstrust.database;

import pl.coderstrust.exception.NotExistingIdException;
import pl.coderstrust.model.Invoice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryDatabase implements Database {

    private Map<Long, Invoice> invoices = new HashMap<>();

    @Override
    public Collection<Invoice> getInvoices() {
        return invoices.values();
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        invoices.put(invoice.getId(), invoice);
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        deleteInvoiceById(invoice.getId());
        saveInvoice(invoice);
    }

    @Override
    public Optional<Invoice> getInvoiceById(Long id) {
        return Optional.ofNullable(invoices.get(id));
    }

    @Override
    public void deleteInvoiceById(Long id) {
        if (!invoices.containsKey(id)) {
            throw new NotExistingIdException("Given ID doesn't exist");
        }
        invoices.remove(id);
    }
}
