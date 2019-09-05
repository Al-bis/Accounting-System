package pl.coderstrust.service;

import pl.coderstrust.model.Invoice;
import pl.coderstrust.persistatnce.InvoiceRepository;

import java.util.Collection;

class InvoiceService {

    private InvoiceRepository database;

    public InvoiceService(InvoiceRepository database) {
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
