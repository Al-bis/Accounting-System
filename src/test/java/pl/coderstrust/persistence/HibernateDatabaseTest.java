package pl.coderstrust.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static pl.coderstrust.persistence.Vat.VAT_23;
import static pl.coderstrust.persistence.Vat.VAT_8;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderstrust.Application;
import pl.coderstrust.domain.InvoiceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class HibernateDatabaseTest {

    @Autowired
    private InvoiceRepository database;
    @Autowired
    private CrudInvoiceRepository repository;

    @BeforeEach
    void init() {
        database = new HibernateDatabase(repository);
    }

    @Test
    void shouldThrownExceptionWhenTryToSaveNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            database.saveInvoice(null);
        });
    }

    @Test
    void shouldThrownExceptionWhenExecuteGetInvoiceWithInvalidId() {
        Assertions.assertThrows(InvoiceNotFoundException.class, () -> {
            database.getInvoice(1L);
        });
    }

    @Test
    void shouldThrownExceptionWhenTryToDeleteNotExistInvoice() {
        Assertions.assertThrows(InvoiceNotFoundException.class, () -> {
            database.deleteInvoice(1L);
        });
    }

    @Test
    void shouldThrownExceptionWhenTryToGetAllInvoicesWithNullFromDate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            database.getInvoices(null, LocalDate.of(2019, 3, 10));
        });
    }

    @Test
    void shouldThrownExceptionWhenTryToGetAllInvoicesWithNullToDate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            database.getInvoices(LocalDate.of(2019, 3, 10), null);
        });
    }

    @Test
    void shouldThrownExceptionWhenTryToGetAllInvoicesWithToDateEarlierThenFromDate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            database.getInvoices(LocalDate.of(2019, 3, 10),
                LocalDate.of(2018, 7, 21));
        });
    }

    @Test
    void shouldSaveInvoiceWhichDoesNotHaveIdAndReturnThisInvoiceId() {
        // given
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        Company company3 = new Company.CompanyBuilder().name("A1").taxIdentificationNumber("11")
            .address("a1").build();
        Company company4 = new Company.CompanyBuilder().name("B1").taxIdentificationNumber("21")
            .address("b1").build();
        InvoiceEntry invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(2L).build();
        InvoiceEntry invoiceEntry2 = new InvoiceEntry.InvoiceEntryBuilder().title("C1")
            .value(new BigDecimal("22.34")).vat(VAT_8).amount(3L).build();
        Invoice invoice1 = new Invoice.InvoiceBuilder().date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        Invoice invoice2 = new Invoice.InvoiceBuilder().date(LocalDate.now()).buyer(company3)
            .seller(company4).entries(Arrays.asList(invoiceEntry2)).build();

        // when
        Long invoiceId1 = database.saveInvoice(invoice1);
        Long invoiceId2 = database.saveInvoice(invoice1);
        Long invoiceId3 = database.saveInvoice(invoice2);

        // then
        assertEquals((Long) 1L, invoiceId1);
        assertEquals((Long) 1L, invoiceId2);
        assertEquals((Long) 5L, invoiceId3);
    }


    @Test
    void shouldSaveAndGetInvoice() {
        // given
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        InvoiceEntry invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(2L).build();
        Invoice invoice1 = new Invoice.InvoiceBuilder().date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();

        // when
        Long invoiceId = database.saveInvoice(invoice1);
        Invoice expected = database.getInvoice(1L);

        //then
        assertAll(
            () -> assertEquals(invoice1.getId(), invoiceId),
            () -> assertEquals(invoice1.getId(), database.getInvoice(1L).getId()),
            () -> assertEquals(invoice1.getDate(), database.getInvoice(1L).getDate()),
            () -> assertEquals(invoice1.getBuyer().getId(), database.getInvoice(1L)
                .getBuyer().getId()),
            () -> assertEquals(invoice1.getBuyer().getAddress(), database.getInvoice(1L)
                .getBuyer().getAddress()),
            () -> assertEquals(invoice1.getBuyer().getName(), database.getInvoice(1L)
                .getBuyer().getName()),
            () -> assertEquals(invoice1.getBuyer().getTaxIdentificationNumber(), database
                .getInvoice(1L).getBuyer().getTaxIdentificationNumber()),
            () -> assertEquals(invoice1.getSeller().getId(), database.getInvoice(1L)
                .getSeller().getId()),
            () -> assertEquals(invoice1.getSeller().getAddress(), database.getInvoice(1L)
                .getSeller().getAddress()),
            () -> assertEquals(invoice1.getSeller().getName(), database.getInvoice(1L)
                .getSeller().getName()),
            () -> assertEquals(invoice1.getSeller().getTaxIdentificationNumber(), database
                .getInvoice(1L).getSeller().getTaxIdentificationNumber()),
            () -> assertEquals(invoice1.getEntries().size(), database.getInvoice(1L)
                .getEntries().size())
        );
    }

    @Test
    void shouldReturnAllInvoicesInGivenDateRange() {
        // given
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        Company company3 = new Company.CompanyBuilder().name("A1").taxIdentificationNumber("11")
            .address("a1").build();
        Company company4 = new Company.CompanyBuilder().name("B1").taxIdentificationNumber("21")
            .address("b1").build();
        Company company5 = new Company.CompanyBuilder().name("A2").taxIdentificationNumber("12")
            .address("a2").build();
        Company company6 = new Company.CompanyBuilder().name("B2").taxIdentificationNumber("22")
            .address("b2").build();
        InvoiceEntry invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(2L).build();
        InvoiceEntry invoiceEntry2 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("D")
            .value(new BigDecimal("10.00")).vat(VAT_23).amount(4L).build();
        InvoiceEntry invoiceEntry3 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("E")
            .value(new BigDecimal("21.06")).vat(VAT_23).amount(9L).build();
        Invoice invoice1 = new Invoice.InvoiceBuilder().id(1L)
            .date(LocalDate.of(2018, 4, 9)).buyer(company1).seller(company2)
            .entries(Arrays.asList(invoiceEntry1)).build();
        Invoice invoice2 = new Invoice.InvoiceBuilder().id(2L)
            .date(LocalDate.of(2019, 1, 28)).buyer(company3)
            .seller(company4).entries(Arrays.asList(invoiceEntry2)).build();
        Invoice invoice3 = new Invoice.InvoiceBuilder().id(3L)
            .date(LocalDate.of(2019, 5, 3)).buyer(company5).seller(company6)
            .entries(Arrays.asList(invoiceEntry3)).build();
        database.saveInvoice(invoice1);
        database.saveInvoice(invoice2);
        database.saveInvoice(invoice3);

        // when
        Collection<Invoice> actual = database
            .getInvoices(LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 4, 29));

        //then
        assertEquals(actual.size(), 1);
    }

    @Test
    void shouldReturnAllInvoices() {
        // given
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        Company company3 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company4 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        InvoiceEntry invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(3L).build();
        InvoiceEntry invoiceEntry2 = new InvoiceEntry.InvoiceEntryBuilder().title("D")
            .value(new BigDecimal("10.00")).vat(VAT_23).amount(4L).build();
        Invoice invoice1 = new Invoice.InvoiceBuilder().date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        Invoice invoice2 = new Invoice.InvoiceBuilder().date(LocalDate.now()).buyer(company3)
            .seller(company4).entries(Arrays.asList(invoiceEntry2)).build();
        database.saveInvoice(invoice1);
        database.saveInvoice(invoice2);
        Collection<Invoice> expected = Arrays.asList(invoice1, invoice2);

        // when
        Collection<Invoice> actual = database.getAllInvoices();

        // then
        assertEquals(actual.size(), 2);
    }

    @Test
    void shouldSaveAndDeleteInvoice() {
        //given
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        Company company3 = new Company.CompanyBuilder().name("A1").taxIdentificationNumber("11")
            .address("a1").build();
        Company company4 = new Company.CompanyBuilder().name("B1").taxIdentificationNumber("21")
            .address("b1").build();
        InvoiceEntry invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(3L).build();
        InvoiceEntry invoiceEntry2 = new InvoiceEntry.InvoiceEntryBuilder().title("D")
            .value(new BigDecimal("10.00")).vat(VAT_23).amount(4L).build();
        Invoice invoice1 = new Invoice.InvoiceBuilder().date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        Invoice invoice2 = new Invoice.InvoiceBuilder().date(LocalDate.now()).buyer(company3)
            .seller(company4).entries(Arrays.asList(invoiceEntry2)).build();
        database.saveInvoice(invoice1);
        database.saveInvoice(invoice2);

        // when
        database.deleteInvoice(5L);

        //then
        assertAll(
            () -> assertEquals(1, database.getAllInvoices().size()),
            () -> assertEquals(invoice1.getId(), database.getInvoice(1L).getId()),
            () -> assertEquals(invoice1.getDate(), database.getInvoice(1L).getDate()),
            () -> assertEquals(invoice1.getBuyer().getId(), database.getInvoice(1L)
                .getBuyer().getId()),
            () -> assertEquals(invoice1.getBuyer().getAddress(), database.getInvoice(1L)
                .getBuyer().getAddress()),
            () -> assertEquals(invoice1.getBuyer().getName(), database.getInvoice(1L)
                .getBuyer().getName()),
            () -> assertEquals(invoice1.getBuyer().getTaxIdentificationNumber(), database
                .getInvoice(1L).getBuyer().getTaxIdentificationNumber()),
            () -> assertEquals(invoice1.getSeller().getId(), database.getInvoice(1L)
                .getSeller().getId()),
            () -> assertEquals(invoice1.getSeller().getAddress(), database.getInvoice(1L)
                .getSeller().getAddress()),
            () -> assertEquals(invoice1.getSeller().getName(), database.getInvoice(1L)
                .getSeller().getName()),
            () -> assertEquals(invoice1.getSeller().getTaxIdentificationNumber(), database
                .getInvoice(1L).getSeller().getTaxIdentificationNumber()),
            () -> assertEquals(invoice1.getEntries().size(), database.getInvoice(1L)
                .getEntries().size())
        );
    }

    @Test
    void shouldUpdateInvoice() {
        // give
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        InvoiceEntry entry1 = new InvoiceEntry.InvoiceEntryBuilder().title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(3L).build();
        InvoiceEntry entry2 = new InvoiceEntry.InvoiceEntryBuilder().title("D")
            .value(new BigDecimal("10.00")).vat(VAT_23).amount(4L).build();
        Invoice invoice1 = new Invoice.InvoiceBuilder().date(LocalDate.now()).buyer(company1)
            .seller(company2).entries(Arrays.asList(entry1)).build();
        Invoice invoice2 = new Invoice.InvoiceBuilder().id(1L)
            .date(LocalDate.of(2018, 4, 9)).buyer(company1)
            .seller(company2).entries(Arrays.asList(entry2)).build();
        database.saveInvoice(invoice1);
        database.saveInvoice(invoice2);

        // when
        Invoice expected = database.getInvoice(1L);

        //then
        assertAll(
            () -> assertFalse(invoice1.getDate().isEqual(expected.getDate())),
            () -> assertEquals(1, database.getAllInvoices().size())
        );
    }

}
