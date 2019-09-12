package pl.coderstrust.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.service.model.Vat.VAT_23;
import static pl.coderstrust.service.model.Vat.VAT_5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.service.model.Company;
import pl.coderstrust.service.model.Invoice;
import pl.coderstrust.service.model.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

class InvoiceValueCalculatorTest {

    private Invoice invoice1;
    private Invoice invoice2;
    private Collection<Invoice> invoices;

    @BeforeEach
    void init() {
        Company company1 = Company.builder()
            .name("A")
            .taxIdentificationNumber("123")
            .address("ABC")
            .build();
        Company company2 = Company.builder()
            .name("B")
            .taxIdentificationNumber("321")
            .address("XYZ")
            .build();
        InvoiceEntry entry1 = InvoiceEntry.builder()
            .id(1L)
            .title("Monitor")
            .value(new BigDecimal("999.89"))
            .vat(VAT_5)
            .amount(2L)
            .build();
        InvoiceEntry entry2 = InvoiceEntry.builder()
            .id(2L)
            .title("Keyboard")
            .value(new BigDecimal("121.19"))
            .vat(VAT_23)
            .amount(6L)
            .build();
        InvoiceEntry entry3 = InvoiceEntry.builder()
            .id(1L)
            .title("Processor")
            .value(new BigDecimal("1100.99"))  // 1045.94
            .vat(VAT_5)
            .amount(1L)
            .build();
        InvoiceEntry entry4 = InvoiceEntry.builder()
            .id(2L)
            .title("Graphics Card")
            .value(new BigDecimal("1599.99"))  // 1231,99 -> 4741.91
            .vat(VAT_23)
            .amount(3L)
            .build();
        invoice1 = Invoice.builder()
            .id(1L)
            .date(LocalDate.now())
            .seller(company1)
            .buyer(company2)
            .entries(Arrays.asList(entry1, entry2))
            .build();
        invoice2 = Invoice.builder()
            .id(2L)
            .date(LocalDate.now())
            .seller(company2)
            .buyer(company1)
            .entries(Arrays.asList(entry3, entry4))
            .build();
        invoices = Arrays.asList(invoice1, invoice2);
    }

    @Test
    public void shouldReturnTotalValue() {
        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValue(invoice1);

        // then
        assertEquals(new BigDecimal("2726.92"), total);
    }

    @Test
    public void shouldReturnTotalValueAfterTax() {
        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValueAfterTax(invoice1);

        // then
        assertEquals(new BigDecimal("2459.70"), total);
    }

    @Test
    public void shouldReturnTotalValueForMoreThenOneInvoice() {
        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValue(invoices);

        // then
        assertEquals(new BigDecimal("8627.88"), total);
    }

    @Test
    public void shouldReturnTotalValueAfterTaxForMoreThenOneInvoice() {
        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValueAfterTax(invoices);

        // then
        assertEquals(new BigDecimal("7201.63"), total);
    }

}
