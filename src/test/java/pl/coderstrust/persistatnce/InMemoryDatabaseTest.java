package pl.coderstrust.persistatnce;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static pl.coderstrust.model.Vat.VAT_23;
import static pl.coderstrust.model.Vat.VAT_5;
import static pl.coderstrust.model.Vat.VAT_8;
import static pl.coderstrust.model.Vat.VAT_ZW;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.exception.InvoiceAlreadyExistException;
import pl.coderstrust.exception.InvoiceNotFoundException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

class InMemoryDatabaseTest {

    private InvoiceRepository database;
    private Invoice invoice1;
    private Invoice invoice2;
    private Invoice invoice3;

    @BeforeEach
    void init() {
        database = new InMemoryDatabase();
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
            .vat(VAT_ZW)
            .build();
        InvoiceEntry entry2 = InvoiceEntry.builder()
            .id(2L)
            .title("Keyboard")
            .value(new BigDecimal("121.19"))
            .vat(VAT_8)
            .build();
        InvoiceEntry entry3 = InvoiceEntry.builder()
            .id(1L)
            .title("Processor")
            .value(new BigDecimal("1100.99"))
            .vat(VAT_5)
            .build();
        InvoiceEntry entry4 = InvoiceEntry.builder()
            .id(2L)
            .title("Graphics Card")
            .value(new BigDecimal("1599.99"))
            .vat(VAT_23)
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
        invoice3 = Invoice.builder()
            .id(1L)
            .date(LocalDate.now())
            .seller(company2)
            .buyer(company1)
            .entries(Arrays.asList(entry3, entry4))
            .build();
    }

    @Test
    public void shouldThrownExceptionWhenTryToSaveNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            database.saveInvoice(null);
        });
    }

    @Test
    public void shouldThrownExceptionWhenExecuteGetInvoiceWithInvalidId() {
        Assertions.assertThrows(InvoiceNotFoundException.class, () -> {
            database.getInvoice(1L);
        });
    }

    @Test
    public void shouldThrownExceptionWhenTryToSaveAlreadyExistInvoice() {
        database.saveInvoice(invoice1);
        Assertions.assertThrows(InvoiceAlreadyExistException.class, () -> {
            database.saveInvoice(invoice1);
        });
    }

    @Test
    public void shouldThrownExceptionWhenTryToDeleteNotExistInvoice() {
        Assertions.assertThrows(InvoiceNotFoundException.class, () -> {
            database.deleteInvoice(1L);
        });
    }

    @Test
    public void shouldSaveAndGetInvoice() {
        // given
        database.saveInvoice(invoice1);

        // when
        Invoice expected = database.getInvoice(1L);

        //then
        assertEquals(invoice1, expected);
    }

    @Test
    public void shouldReturnAllInvoices() {
        // given
        database.saveInvoice(invoice1);
        database.saveInvoice(invoice2);
        Collection<Invoice> expected = Arrays.asList(invoice1, invoice2);

        // when
        Collection<Invoice> actual = database.getAllInvoices();

        // then
        assertIterableEquals(actual, expected);
    }

    @Test
    public void shouldSaveAndDeleteInvoice() {
        //given
        database.saveInvoice(invoice1);
        database.saveInvoice(invoice2);

        // when
        database.deleteInvoice(2L);

        //then
        assertAll(
            () -> assertEquals(1, database.getAllInvoices().size()),
            () -> assertEquals(invoice1, database.getInvoice(1L))
        );
    }

    @Test
    public void shouldUpdateInvoice() {
        // give
        database.saveInvoice(invoice1);
        database.saveInvoice(invoice3);

        // when
        Invoice expected = database.getInvoice(1L);

        //then
        assertAll(
            () -> assertEquals(invoice3, expected),
            () -> assertEquals(1, database.getAllInvoices().size())
        );
    }
}
