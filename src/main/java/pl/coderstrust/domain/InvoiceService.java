package pl.coderstrust.domain;

import org.springframework.stereotype.Service;
import pl.coderstrust.persistence.InvoiceRepository;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class InvoiceService {

    private InvoiceRepository database;

    public InvoiceService(InvoiceRepository database) {
        if (database == null) {
            throw new IllegalArgumentException("Given database cannot be null");
        }
        this.database = database;
    }

    public Collection<Invoice> getAllInvoices() {
        return ModelMapperConverter.modelMapperConverter.convertAll(database.getAllInvoices());
    }

    public Collection<Invoice> getAllInvoices(LocalDate fromDate, LocalDate toDate) {
        return ModelMapperConverter.modelMapperConverter
            .convertAll(database.getInvoices(fromDate, toDate));
    }

    public Long saveInvoice(Invoice invoice) {
        return database
            .saveInvoice(ModelMapperConverter.modelMapperConverter
                .convertToPersistatnceInvoice(invoice));
    }

    public Invoice getInvoice(Long id) {
        return ModelMapperConverter.modelMapperConverter.convert(database.getInvoice(id));
    }

    public void deleteInvoice(Long id) {
        database.deleteInvoice(id);
    }
}
