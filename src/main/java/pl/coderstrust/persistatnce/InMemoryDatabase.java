package pl.coderstrust.persistatnce;

import pl.coderstrust.domain.Invoice;
import pl.coderstrust.domain.InvoiceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

class InMemoryDatabase implements InvoiceRepository {

    private Map<Long, Invoice> invoices = new ConcurrentHashMap<>();
    private AtomicLong invoiceId = new AtomicLong();

    @Override
    public Collection<Invoice> getAllInvoices() {
        return Collections.unmodifiableCollection(invoices.values());
    }

    @Override
    public Collection<Invoice> getInvoices(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) {
            throw new IllegalArgumentException("Given fromDate cannot be null");
        }
        if (toDate == null) {
            throw new IllegalArgumentException("Given toDate cannot be null");
        }
        if (toDate.compareTo(fromDate) <= -1) {
            throw new IllegalArgumentException("Given fromDate must be earlier than toDate");
        }
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
        if (invoice == null) {
            throw new IllegalArgumentException("Given invoice cannot be null");
        }
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
