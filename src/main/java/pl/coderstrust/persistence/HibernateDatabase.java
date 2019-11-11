package pl.coderstrust.persistence;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

@Repository
@ConditionalOnProperty(name = "database", havingValue = "hibernate")
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
        DatabaseValidator.validateDatesIfNullOrInIncorrectOrder(fromDate, toDate);
        return repository.findByDateBetween(fromDate, toDate);
    }

    @Override
    public Long saveInvoice(Invoice invoice) {
        DatabaseValidator.validateInvoiceIfNull(invoice);
        return repository.save(invoice).getId();
    }

    @Override
    public Invoice getInvoice(Long id) {
        boolean idIsNotExistInDatabase = !repository.existsById(id);
        DatabaseValidator.validateInvoiceIdIfNull(id, idIsNotExistInDatabase);
        return repository.findById(id).get();
    }

    @Override
    public void deleteInvoice(Long id) {
        boolean idIsNotExistInDatabase = !repository.existsById(id);
        DatabaseValidator.validateInvoiceIdIfNull(id, idIsNotExistInDatabase);
        repository.deleteById(id);
    }
}
