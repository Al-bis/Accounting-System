package pl.coderstrust.utils;

import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;

public class InvoiceValueCalculator {

    public static BigDecimal calculateTotalValue(Invoice invoice) {
        return invoice.getEntries().stream()
            .map(InvoiceEntry::getValue)
            .reduce(BigDecimal.ZERO, (total, next) -> total.add(next));
    }

    public static BigDecimal calculateTotalValueAfterTax(Invoice invoice) {
        return invoice.getEntries().stream()
            .map(InvoiceEntry::getValueAfterTax)
            .reduce(BigDecimal.ZERO, (total, next) -> total.add(next));
    }

}
