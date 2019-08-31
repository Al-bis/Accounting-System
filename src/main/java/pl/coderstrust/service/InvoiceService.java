package pl.coderstrust.service;

import pl.coderstrust.database.Database;
import pl.coderstrust.exception.NotExistingIdException;
import pl.coderstrust.model.Invoice;

import java.util.Collection;

public class InvoiceService {

    private Database database;

    public InvoiceService(Database database) {
        this.database = database;
    }

    public Collection<Invoice> getInvoices() {
        return database.getInvoices();
    }

    public void saveInvoice(Invoice invoice) {
        database.saveInvoice(invoice);
    }

    public void updateInvoice(Invoice invoice) {
        database.updateInvoice(invoice);
    }

    public Invoice getInvoiceById(Long id) {
        if (database.getInvoiceById(id).isEmpty()) {
            throw new NotExistingIdException("Given ID doesn't exist");
        }
        return database.getInvoiceById(id).get();
    }

    public boolean deleteInvoiceById(Long id) {
        try {
            database.deleteInvoiceById(id);
            return true;
        } catch (NotExistingIdException err) {
            return false;
        }
    }
}
