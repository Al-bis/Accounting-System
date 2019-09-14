package pl.coderstrust.calculator;

import pl.coderstrust.domain.Invoice;
import pl.coderstrust.domain.InvoiceEntry;

import java.math.BigDecimal;
import java.util.Collection;

public class InvoiceValueCalculator {

    public static BigDecimal calculateTotalValue(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Given invoice cannot be null");
        }
        return getTotalValue(invoice);
    }

    public static BigDecimal calculateTotalValue(Collection<Invoice> invoices) {
        if (invoices == null) {
            throw new IllegalArgumentException("Given invoices cannot be null");
        }
        BigDecimal invoicesTotalValue = new BigDecimal("0.00");
        for (Invoice invoice : invoices) {
            invoicesTotalValue = invoicesTotalValue.add(getTotalValue(invoice));
        }
        return invoicesTotalValue;
    }

    private static BigDecimal getTotalValue(Invoice invoice) {
        return invoice.getEntries().stream()
            .map(InvoiceEntry::getTotalValue)
            .reduce(BigDecimal.ZERO, (total, next) -> total.add(next));
    }

    public static BigDecimal calculateTotalValueAfterTax(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Given invoice cannot be null");
        }
        return getTotalValueAfterTax(invoice);
    }

    public static BigDecimal calculateTotalValueAfterTax(Collection<Invoice> invoices) {
        if (invoices == null) {
            throw new IllegalArgumentException("Given invoices cannot be null");
        }
        BigDecimal invoicesTotalValue = new BigDecimal("0.00");
        for (Invoice invoice : invoices) {
            invoicesTotalValue = invoicesTotalValue.add(getTotalValueAfterTax(invoice));
        }
        return invoicesTotalValue;
    }

    private static BigDecimal getTotalValueAfterTax(Invoice invoice) {
        return invoice.getEntries().stream()
            .map(InvoiceEntry::getTotalValueAfterTax)
            .reduce(BigDecimal.ZERO, (total, next) -> total.add(next));
    }

}
