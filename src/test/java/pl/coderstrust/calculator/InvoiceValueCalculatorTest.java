package pl.coderstrust.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.domain.Vat.VAT_23;
import static pl.coderstrust.domain.Vat.VAT_5;

import org.junit.jupiter.api.Test;
import pl.coderstrust.domain.Company;
import pl.coderstrust.domain.Invoice;
import pl.coderstrust.domain.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

class InvoiceValueCalculatorTest {

    @Test
    public void shouldReturnTotalValue() {
        // given
        var company1 = Company.builder().name("A").taxIdentificationNumber("123")
            .address("ABC").build();
        var company2 = Company.builder().name("B").taxIdentificationNumber("321")
            .address("XYZ").build();
        var entry1 = InvoiceEntry.builder().id(1L).title("Monitor")
            .value(new BigDecimal("999.89")).vat(VAT_5).amount(2L).build();
        var entry2 = InvoiceEntry.builder().id(2L).title("Keyboard")
            .value(new BigDecimal("121.19")).vat(VAT_23).amount(6L).build();
        var invoice1 = Invoice.builder().id(1L).date(LocalDate.now()).seller(company1)
            .buyer(company2)
            .entries(Arrays.asList(entry1, entry2)).build();

        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValue(invoice1);

        // then
        assertEquals(new BigDecimal("2726.92"), total);
    }

    @Test
    public void shouldReturnTotalValueAfterTax() {
        // given
        var company1 = Company.builder().name("A").taxIdentificationNumber("123")
            .address("ABC").build();
        var company2 = Company.builder().name("B").taxIdentificationNumber("321")
            .address("XYZ").build();
        var entry1 = InvoiceEntry.builder().id(1L).title("Monitor")
            .value(new BigDecimal("999.89")).vat(VAT_5).amount(2L).build();
        var entry2 = InvoiceEntry.builder().id(2L).title("Keyboard")
            .value(new BigDecimal("121.19")).vat(VAT_23).amount(6L).build();
        var invoice1 = Invoice.builder().id(1L).date(LocalDate.now()).seller(company1)
            .buyer(company2)
            .entries(Arrays.asList(entry1, entry2)).build();

        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValueAfterTax(invoice1);

        // then
        assertEquals(new BigDecimal("2459.70"), total);
    }

    @Test
    public void shouldReturnTotalValueForMoreThenOneInvoice() {
        // given
        var company1 = Company.builder().name("A").taxIdentificationNumber("123")
            .address("ABC").build();
        var company2 = Company.builder().name("B").taxIdentificationNumber("321")
            .address("XYZ").build();
        var entry1 = InvoiceEntry.builder().id(1L).title("Monitor")
            .value(new BigDecimal("999.89")).vat(VAT_5).amount(2L).build();
        var entry2 = InvoiceEntry.builder().id(2L).title("Keyboard")
            .value(new BigDecimal("121.19")).vat(VAT_23).amount(6L).build();
        var entry3 = InvoiceEntry.builder().id(1L).title("Processor")
            .value(new BigDecimal("1100.99")).vat(VAT_5).amount(1L).build();
        var entry4 = InvoiceEntry.builder().id(2L).title("Graphics Card")
            .value(new BigDecimal("1599.99")).vat(VAT_23).amount(3L).build();
        var invoice1 = Invoice.builder().id(1L).date(LocalDate.now()).seller(company1)
            .buyer(company2)
            .entries(Arrays.asList(entry1, entry2)).build();
        var invoice2 = Invoice.builder().id(2L).date(LocalDate.now()).seller(company2)
            .buyer(company1)
            .entries(Arrays.asList(entry3, entry4)).build();
        Collection<Invoice> invoices = Arrays.asList(invoice1, invoice2);

        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValue(invoices);

        // then
        assertEquals(new BigDecimal("8627.88"), total);
    }

    @Test
    public void shouldReturnTotalValueAfterTaxForMoreThenOneInvoice() {
        // given
        var company1 = Company.builder().name("A").taxIdentificationNumber("123")
            .address("ABC").build();
        var company2 = Company.builder().name("B").taxIdentificationNumber("321")
            .address("XYZ").build();
        var entry1 = InvoiceEntry.builder().id(1L).title("Monitor")
            .value(new BigDecimal("999.89")).vat(VAT_5).amount(2L).build();
        var entry2 = InvoiceEntry.builder().id(2L).title("Keyboard")
            .value(new BigDecimal("121.19")).vat(VAT_23).amount(6L).build();
        var entry3 = InvoiceEntry.builder().id(1L).title("Processor")
            .value(new BigDecimal("1100.99")).vat(VAT_5).amount(1L).build();
        var entry4 = InvoiceEntry.builder().id(2L).title("Graphics Card")
            .value(new BigDecimal("1599.99")).vat(VAT_23).amount(3L).build();
        var invoice1 = Invoice.builder().id(1L).date(LocalDate.now()).seller(company1)
            .buyer(company2)
            .entries(Arrays.asList(entry1, entry2)).build();
        var invoice2 = Invoice.builder().id(2L).date(LocalDate.now()).seller(company2)
            .buyer(company1)
            .entries(Arrays.asList(entry3, entry4)).build();
        Collection<Invoice> invoices = Arrays.asList(invoice1, invoice2);

        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValueAfterTax(invoices);

        // then
        assertEquals(new BigDecimal("7201.63"), total);
    }

}
