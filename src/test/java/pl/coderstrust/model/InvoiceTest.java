package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.model.Vat.VAT_0;
import static pl.coderstrust.model.Vat.VAT_23;
import static pl.coderstrust.model.Vat.VAT_5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

class InvoiceTest {

    @Test
    void shouldThrownExceptionWhenCreatingInvoiceWithValueBelow1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Invoice(-1L, LocalDate.now(),
                new Company("A", "123"),
                new Company("B", "321"),
                Arrays.asList());
        });
    }

    @Test
    void shouldThrownExceptionWhenCreatingInvoiceWithInvoiceEntryWhichHasIdBelow1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Invoice(-1L, LocalDate.now(),
                new Company("A", "123"),
                new Company("B", "321"),
                Arrays.asList(new InvoiceEntry(-1L, "Monitor", new BigDecimal("999.89"), VAT_0)));
        });
    }

    @Test
    void shouldThrownExceptionWhenCreatingInvoiceWithInvoiceEntryWhichHasNegativeValue() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Invoice(-1L, LocalDate.now(),
                new Company("A", "123"),
                new Company("B", "321"),
                Arrays.asList(new InvoiceEntry(1L, "Monitor", new BigDecimal("-999.89"), VAT_0)));
        });
    }

    @Test
    public void shouldReturnTotalValue() {
        // given
        Invoice invoice = new Invoice(1L, LocalDate.now(),
            new Company("A", "123"),
            new Company("B", "321"),
            Arrays.asList(new InvoiceEntry(1L, "Monitor", new BigDecimal("999.89"), VAT_0),
                new InvoiceEntry(2L, "Keyboard", new BigDecimal("121.19"), VAT_0)));

        // when
        BigDecimal total = invoice.calculateTotalValue();

        // then
        assertEquals(new BigDecimal("1121.08"), total);
    }

    @Test
    public void shouldReturnTotalValueAfterTax() {
        // given
        Invoice invoice = new Invoice(1L, LocalDate.now(),
            new Company("A", "123"),
            new Company("B", "321"),
            Arrays.asList(new InvoiceEntry(1L, "Monitor", new BigDecimal("999.89"), VAT_5),
                new InvoiceEntry(2L, "Keyboard", new BigDecimal("121.19"), VAT_23)));

        // when
        BigDecimal total = invoice.calculateTotalValueAfterTax();

        // then
        assertEquals(new BigDecimal("1043.22"), total);
    }

}
