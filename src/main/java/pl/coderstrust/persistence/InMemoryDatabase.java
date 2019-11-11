package pl.coderstrust.persistence;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@ConditionalOnProperty(name = "database", havingValue = "in-memory")
class InMemoryDatabase implements InvoiceRepository {

    private Map<Long, Invoice> invoices = new ConcurrentHashMap<>();
    private AtomicLong invoiceId = new AtomicLong();

    @Override
    public Collection<Invoice> getAllInvoices() {
        return Collections.unmodifiableCollection(invoices.values());
    }

    @Override
    public Collection<Invoice> getInvoices(LocalDate fromDate, LocalDate toDate) {
        DatabaseValidator.validateDatesIfNullOrInIncorrectOrder(fromDate, toDate);
        Collection<Invoice> invoicesInTimeRange = new ArrayList<>();
        for (Invoice invoice : invoices.values()) {
            if (invoice.getDate().isAfter(fromDate) && invoice.getDate().isBefore(toDate)) {
                invoicesInTimeRange.add(invoice);
            }
        }
        return Collections.unmodifiableCollection(invoicesInTimeRange);
    }

    @Override
    public Long saveInvoice(Invoice invoice) {
        DatabaseValidator.validateInvoiceIfNull(invoice);
        if (invoice.getId() == null || !invoices.containsKey(invoice.getId())) {
            Long invoiceId = this.invoiceId.incrementAndGet();
            Invoice invoiceCopy = new Invoice(invoiceId, invoice);
            invoices.put(invoiceId, invoiceCopy);
            return invoiceId;
        } else {
            Invoice invoiceCopy = new Invoice(invoice);
            Long invoiceId = invoice.getId();
            invoices.remove(invoiceId);
            invoices.put(invoiceId, invoiceCopy);
            return invoiceId;
        }
    }

    @Override
    public Invoice getInvoice(Long id) {
        boolean idIsNotExistInDatabase = !invoices.containsKey(id);
        DatabaseValidator.validateInvoiceIdIfNull(id, idIsNotExistInDatabase);
        return invoices.get(id);
    }

    @Override
    public void deleteInvoice(Long id) {
        boolean idIsNotExistInDatabase = !invoices.containsKey(id);
        DatabaseValidator.validateInvoiceIdIfNull(id, idIsNotExistInDatabase);
        invoices.remove(id);
    }
}
