package pl.coderstrust.persistatnce;

import pl.coderstrust.exception.InvoiceAlreadyExistException;
import pl.coderstrust.exception.InvoiceNotFoundException;
import pl.coderstrust.model.Invoice;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDatabase implements InvoiceRepository {

    private Map<Long, Invoice> invoices = new HashMap<>();

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
            if (invoices.containsValue(invoice)) {
                throw new InvoiceAlreadyExistException("Invoice already exist in data base");
            }
            deleteInvoice(invoice.getId());
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

    @Override
    public void deleteInvoice(Long id) {
        if (!invoices.containsKey(id)) {
            throw new InvoiceNotFoundException("Invoice for id = {" + id + "} is not exists.");
        }
        invoices.remove(id);
    }
}
