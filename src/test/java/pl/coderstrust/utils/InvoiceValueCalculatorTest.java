package pl.coderstrust.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.model.Vat.VAT_23;
import static pl.coderstrust.model.Vat.VAT_5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

class InvoiceValueCalculatorTest {

    private Invoice invoice1;

    @BeforeEach
    void init() {
        Company company1 = Company.builder()
            .name("A")
            .taxIdentificationNumber("123")
            .build();
        Company company2 = Company.builder()
            .name("B")
            .taxIdentificationNumber("321")
            .build();
        InvoiceEntry entry1 = InvoiceEntry.builder()
            .id(1L)
            .title("Monitor")
            .value(new BigDecimal("999.89"))
            .vat(VAT_5)
            .build();
        InvoiceEntry entry2 = InvoiceEntry.builder()
            .id(2L)
            .title("Keyboard")
            .value(new BigDecimal("121.19"))
            .vat(VAT_23)
            .build();
        invoice1 = Invoice.builder()
            .id(1L)
            .date(LocalDate.now())
            .seller(company1)
            .buyer(company2)
            .entries(Arrays.asList(entry1, entry2))
            .build();
    }

    @Test
    public void shouldReturnTotalValue() {
        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValue(invoice1);

        // then
        assertEquals(new BigDecimal("1121.08"), total);
    }

    @Test
    public void shouldReturnTotalValueAfterTax() {
        // when
        BigDecimal total = InvoiceValueCalculator.calculateTotalValueAfterTax(invoice1);

        // then
        assertEquals(new BigDecimal("1043.22"), total);
    }
}
