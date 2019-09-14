package pl.coderstrust.persistatnce;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static pl.coderstrust.domain.Vat.VAT_23;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.domain.Company;
import pl.coderstrust.domain.Invoice;
import pl.coderstrust.domain.InvoiceEntry;
import pl.coderstrust.domain.InvoiceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

class InMemoryDatabaseTest {

    private InvoiceRepository database;

    @BeforeEach
    void init() {
        database = new InMemoryDatabase();
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
    public void shouldThrownExceptionWhenTryToDeleteNotExistInvoice() {
        Assertions.assertThrows(InvoiceNotFoundException.class, () -> {
            database.deleteInvoice(1L);
        });
    }

    @Test
    public void shouldSaveAndGetInvoice() {
        // given
        var company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        var company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        var invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(2L).build();
        var invoice1 = new Invoice.InvoiceBuilder().id(1L).date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        database.saveInvoice(invoice1);

        // when
        Invoice expected = database.getInvoice(1L);

        //then
        assertEquals(invoice1, expected);
    }

    @Test
    public void shouldReturnAllInvoices() {
        // given
        var company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        var company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        var invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(3L).build();
        var invoiceEntry2 = new InvoiceEntry.InvoiceEntryBuilder().id(2L).title("D")
            .value(new BigDecimal("10.00")).vat(VAT_23).amount(4L).build();
        var invoice1 = new Invoice.InvoiceBuilder().id(1L).date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        var invoice2 = new Invoice.InvoiceBuilder().id(2L).date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry2)).build();
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
        var company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        var company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        var invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(3L).build();
        var invoiceEntry2 = new InvoiceEntry.InvoiceEntryBuilder().id(2L).title("D")
            .value(new BigDecimal("10.00")).vat(VAT_23).amount(4L).build();
        var invoice1 = new Invoice.InvoiceBuilder().id(1L).date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        var invoice2 = new Invoice.InvoiceBuilder().id(2L).date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry2)).build();
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
        var company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        var company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        var entry1 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(3L).build();
        var entry2 = new InvoiceEntry.InvoiceEntryBuilder().id(2L).title("D")
            .value(new BigDecimal("10.00")).vat(VAT_23).amount(4L).build();
        var invoice1 = new Invoice.InvoiceBuilder().id(1L).date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(entry1)).build();
        var invoice2 = new Invoice.InvoiceBuilder().id(1L).date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(entry2)).build();
        database.saveInvoice(invoice1);
        database.saveInvoice(invoice2);

        // when
        Invoice expected = database.getInvoice(1L);

        //then
        assertAll(
            () -> assertEquals(invoice2, expected),
            () -> assertEquals(1, database.getAllInvoices().size())
        );
    }

}
