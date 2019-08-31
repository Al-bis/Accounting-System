package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static pl.coderstrust.model.Vat.VAT_23;
import static pl.coderstrust.model.Vat.VAT_5;
import static pl.coderstrust.model.Vat.VAT_8;
import static pl.coderstrust.model.Vat.VAT_ZW;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.InMemoryDatabase;
import pl.coderstrust.exception.NotExistingIdException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

class InvoiceServiceTest {

    @ParameterizedTest
    @MethodSource("databaseProvider")
    public void shouldThrownExceptionWhenExecuteGetInvoiceWithInvalidId(Database database) {
        InvoiceService invoiceService = new InvoiceService(database);
        Assertions.assertThrows(NotExistingIdException.class, () -> {
            invoiceService.getInvoiceById(1L);
        });
    }

    @ParameterizedTest
    @MethodSource("databaseProvider")
    public void shouldSaveAndGetInvoice(Database database) {
        // given
        InvoiceService invoiceService = new InvoiceService(database);
        Invoice actual = new Invoice(1L, LocalDate.now(),
            new Company("A", "123"),
            new Company("B", "321"),
            Arrays.asList(new InvoiceEntry(1L, "Monitor", new BigDecimal("999.89"), VAT_ZW),
                new InvoiceEntry(2L, "Keyboard", new BigDecimal("121.19"), VAT_8)));
        invoiceService.saveInvoice(actual);

        // when
        Invoice expected = invoiceService.getInvoiceById(1L);

        //then
        assertEquals(actual, expected);
    }

    @ParameterizedTest
    @MethodSource("databaseProvider")
    public void shouldReturnAllInvoices(Database database) {
        // given
        InvoiceService invoiceService = new InvoiceService(database);
        Invoice invoice1 = new Invoice(1L, LocalDate.now(),
            new Company("A", "123"),
            new Company("B", "321"),
            Arrays.asList(new InvoiceEntry(1L, "Monitor", new BigDecimal("999.89"), VAT_ZW),
                new InvoiceEntry(2L, "Keyboard", new BigDecimal("121.19"), VAT_8)));
        Invoice invoice2 = new Invoice(2L, LocalDate.now(),
            new Company("A", "123"),
            new Company("B", "321"),
            Arrays.asList(new InvoiceEntry(1L, "Processor", new BigDecimal("1100.99"), VAT_5),
                new InvoiceEntry(2L, "Graphics Card", new BigDecimal("1599.99"), VAT_23)));
        invoiceService.saveInvoice(invoice1);
        invoiceService.saveInvoice(invoice2);
        Collection<Invoice> expected = Arrays.asList(invoice1, invoice2);

        // when
        Collection<Invoice> actual = invoiceService.getInvoices();

        // then
        assertIterableEquals(actual, expected);
    }

    @ParameterizedTest
    @MethodSource("databaseProvider")
    public void shouldSaveAndDeleteInvoice(Database database) {
        // given
        InvoiceService invoiceService = new InvoiceService(database);
        Invoice invoice1 = new Invoice(1L, LocalDate.now(),
            new Company("A", "123"),
            new Company("B", "321"),
            Arrays.asList(new InvoiceEntry(1L, "Monitor", new BigDecimal("999.89"), VAT_ZW),
                new InvoiceEntry(2L, "Keyboard", new BigDecimal("121.19"), VAT_8)));
        Invoice invoice2 = new Invoice(2L, LocalDate.now(),
            new Company("A", "123"),
            new Company("B", "321"),
            Arrays.asList(new InvoiceEntry(1L, "Processor", new BigDecimal("1100.99"), VAT_5),
                new InvoiceEntry(2L, "Graphics Card", new BigDecimal("1599.99"), VAT_23)));
        invoiceService.saveInvoice(invoice1);
        invoiceService.saveInvoice(invoice2);

        // when
        boolean isInvoiceDeleted = invoiceService.deleteInvoiceById(2L);

        //then
        assertTrue(isInvoiceDeleted);
        assertFalse(invoiceService.deleteInvoiceById(2L));
    }

    @ParameterizedTest
    @MethodSource("databaseProvider")
    public void shouldUpdateInvoice(Database database) {
        // given
        InvoiceService invoiceService = new InvoiceService(database);
        Invoice invoice1 = new Invoice(1L, LocalDate.now(),
            new Company("A", "123"),
            new Company("B", "321"),
            Arrays.asList(new InvoiceEntry(1L, "Monitor", new BigDecimal("999.89"), VAT_ZW),
                new InvoiceEntry(2L, "Keyboard", new BigDecimal("121.19"), VAT_8)));
        Invoice invoice2 = new Invoice(1L, LocalDate.now(),
            new Company("A", "123"),
            new Company("B", "321"),
            Arrays.asList(new InvoiceEntry(1L, "Processor", new BigDecimal("1100.99"), VAT_5),
                new InvoiceEntry(2L, "Graphics Card", new BigDecimal("1599.99"), VAT_23)));
        invoiceService.saveInvoice(invoice1);

        // when
        invoiceService.updateInvoice(invoice2);
        Invoice expected = invoiceService.getInvoiceById(1L);

        //then
        assertEquals(invoice2, expected);
    }

    static Stream<Arguments> databaseProvider() {
        return Stream.of(
            arguments(new InMemoryDatabase())
        );
    }

}
