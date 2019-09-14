package pl.coderstrust.persistatnce;

import pl.coderstrust.domain.Invoice;
import pl.coderstrust.domain.InvoiceNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDatabase implements InvoiceRepository {

    private Map<Long, Invoice> invoices = new ConcurrentHashMap<>();

    @Override
    public Collection<Invoice> getAllInvoices() {
        return List.copyOf(invoices.values());
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Given invoice cannot be null");
        }
        Invoice invoiceCopy = new Invoice(invoice);
        if (invoices.containsKey(invoice.getId())) {
            deleteInvoiceWithoutValidation(invoice.getId());
            invoices.put(invoice.getId(), invoiceCopy);
        } else {
            invoices.put(invoice.getId(), invoiceCopy);
        }
    }

    @Override
    public Invoice getInvoice(Long id) {
        if (!invoices.containsKey(id)) {
            throw new InvoiceNotFoundException("Invoice for id = {" + id + "} is not exists.");
        }
        return invoices.get(id);
    }

    private void deleteInvoiceWithoutValidation(Long id) {
        invoices.remove(id);
    }

    @Override
    public void deleteInvoice(Long id) {
        if (!invoices.containsKey(id)) {
            throw new InvoiceNotFoundException("Invoice for id = {" + id + "} is not exists.");
        }
        invoices.remove(id);
    }
}
