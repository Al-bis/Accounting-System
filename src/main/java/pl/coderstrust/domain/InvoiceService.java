package pl.coderstrust.domain;

import pl.coderstrust.persistatnce.InvoiceRepository;

import java.time.LocalDate;
import java.util.Collection;

public class InvoiceService {

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

    public Collection<Invoice> getAllInvoices(LocalDate fromDate, LocalDate toDate) {
        return database.getInvoices(fromDate, toDate);
    }

    public Long saveInvoice(Invoice invoice) {
        return database.saveInvoice(invoice);
    }

    public Invoice getInvoice(Long id) {
        return database.getInvoice(id);
    }

    public void deleteInvoice(Long id) {
        database.deleteInvoice(id);
    }
}
