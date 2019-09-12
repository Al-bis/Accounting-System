package pl.coderstrust.service;

import pl.coderstrust.persistatnce.InvoiceRepository;
import pl.coderstrust.service.model.Invoice;

import java.util.Collection;

class InvoiceService {

    private InvoiceRepository database;

    public InvoiceService(InvoiceRepository database) {
        if (database == null) {
            throw new IllegalArgumentException("Given database cannot be null");
        }
        this.database = database;
    }

    public Collection<Invoice> getAllInvoices() {
        return database.getAllInvoices();
    }

    public void saveInvoice(Invoice invoice) {
        database.saveInvoice(invoice);
    }

    public Invoice getInvoice(Long id) {
        return database.getInvoice(id);
    }

    public void deleteInvoice(Long id) {
        database.deleteInvoice(id);
    }
}
