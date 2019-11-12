package pl.coderstrust.persistence;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.domain.InvoiceNotFoundException;

import java.time.LocalDate;
import java.util.Collection;

@Repository
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "hibernate")
class HibernateDatabase implements InvoiceRepository {

    private final CrudInvoiceRepository repository;

    public HibernateDatabase(CrudInvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Invoice> getAllInvoices() {
        return (Collection<Invoice>) repository.findAll();
    }

    @Override
    public Collection<Invoice> getInvoices(LocalDate fromDate, LocalDate toDate) {
        DatabaseValidator.checkRangeOfDates(fromDate, toDate);
        return repository.findByDateBetween(fromDate, toDate);
    }

    @Override
    public Long saveInvoice(Invoice invoice) {
        DatabaseValidator.checkInvoice(invoice);
        return repository.save(invoice).getId();
    }

    @Override
    public Invoice getInvoice(Long id) {
        if (!repository.existsById(id)) {
            throw new InvoiceNotFoundException("Invoice for id = {" + id + "} is not exists.");
        }
        return repository.findById(id).get();
    }

    @Override
    public void deleteInvoice(Long id) {
        if (!repository.existsById(id)) {
            throw new InvoiceNotFoundException("Invoice for id = {" + id + "} is not exists.");
        }
        repository.deleteById(id);
    }
}
