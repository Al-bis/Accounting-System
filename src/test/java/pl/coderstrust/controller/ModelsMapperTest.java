package pl.coderstrust.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.controller.Vat.VAT_23;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

class ModelsMapperTest {

    @Test
    void shouldReturnSameFieldsValuesAfterMappingInvoiceFromDomainToController() {
        // given
        pl.coderstrust.domain.Company company1 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("A").taxIdentificationNumber("1").address("a").build();
        pl.coderstrust.domain.Company company2 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("B").taxIdentificationNumber("2").address("b").build();
        pl.coderstrust.domain.InvoiceEntry invoiceEntry1 = new pl.coderstrust.domain.InvoiceEntry
            .InvoiceEntryBuilder().id(1L).title("C").value(new BigDecimal("12.34"))
            .vat(pl.coderstrust.domain.Vat.VAT_23).amount(2L).build();
        pl.coderstrust.domain.Invoice domainInvoice = new pl.coderstrust.domain.Invoice
            .InvoiceBuilder().id(1L).date(LocalDate.of(2018, 4, 9))
            .buyer(company1).seller(company2).entries(Arrays.asList(invoiceEntry1)).build();

        // when
        Invoice controllerInvoice = ModelsMapper.mapInvoice(domainInvoice);

        // then
        assertEquals(controllerInvoice.getId(), domainInvoice.getId());
        assertEquals(controllerInvoice.getDate(), domainInvoice.getDate());
        assertEquals(controllerInvoice.getBuyer().getName(), domainInvoice.getBuyer().getName());
        assertEquals(controllerInvoice.getBuyer().getAddress(), domainInvoice.getBuyer()
            .getAddress());
        assertEquals(controllerInvoice.getBuyer().getTaxIdentificationNumber(), domainInvoice
            .getBuyer().getTaxIdentificationNumber());
        assertEquals(controllerInvoice.getSeller().getName(), domainInvoice.getSeller().getName());
        assertEquals(controllerInvoice.getSeller().getAddress(), domainInvoice.getSeller()
            .getAddress());
        assertEquals(controllerInvoice.getSeller().getTaxIdentificationNumber(), domainInvoice
            .getSeller().getTaxIdentificationNumber());
        assertEquals(controllerInvoice.getEntries().size(), domainInvoice.getEntries().size());
        for (int i = 0; i < domainInvoice.getEntries().size(); i++) {
            assertEquals(controllerInvoice.getEntries().get(i).getId(), domainInvoice.getEntries()
                .get(i).getId());
            assertEquals(controllerInvoice.getEntries().get(i).getTitle(), domainInvoice
                .getEntries().get(i).getTitle());
            assertEquals(controllerInvoice.getEntries().get(i).getValue(), domainInvoice
                .getEntries().get(i).getValue());
            assertEquals(controllerInvoice.getEntries().get(i).getVat().getValue(), domainInvoice
                .getEntries().get(i).getVat().getValue());
            assertEquals(controllerInvoice.getEntries().get(i).getAmount(), domainInvoice
                .getEntries().get(i).getAmount());
        }
    }

    @Test
    void shouldReturnSameFieldsValuesAfterMappingInvoiceFromControllerToDomain() {
        // given
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        InvoiceEntry invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(2L).build();
        Invoice controllerInvoice = new Invoice.InvoiceBuilder().id(1L)
            .date(LocalDate.of(2018, 4, 9)).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();

        // when
        pl.coderstrust.domain.Invoice domainInvoice = ModelsMapper
            .mapInvoiceToDomain(controllerInvoice);

        // then
        assertEquals(domainInvoice.getId(), controllerInvoice.getId());
        assertEquals(domainInvoice.getDate(), controllerInvoice.getDate());
        assertEquals(domainInvoice.getBuyer().getName(), controllerInvoice.getBuyer().getName());
        assertEquals(domainInvoice.getBuyer().getAddress(), controllerInvoice.getBuyer()
            .getAddress());
        assertEquals(domainInvoice.getBuyer().getTaxIdentificationNumber(), controllerInvoice
            .getBuyer().getTaxIdentificationNumber());
        assertEquals(domainInvoice.getSeller().getName(), controllerInvoice.getSeller().getName());
        assertEquals(domainInvoice.getSeller().getAddress(), controllerInvoice.getSeller()
            .getAddress());
        assertEquals(domainInvoice.getSeller().getTaxIdentificationNumber(), controllerInvoice
            .getSeller().getTaxIdentificationNumber());
        assertEquals(domainInvoice.getEntries().size(), controllerInvoice.getEntries().size());
        for (int i = 0; i < controllerInvoice.getEntries().size(); i++) {
            assertEquals(domainInvoice.getEntries().get(i).getId(), controllerInvoice.getEntries()
                .get(i).getId());
            assertEquals(domainInvoice.getEntries().get(i).getTitle(), controllerInvoice
                .getEntries().get(i).getTitle());
            assertEquals(domainInvoice.getEntries().get(i).getValue(), controllerInvoice
                .getEntries().get(i).getValue());
            assertEquals(domainInvoice.getEntries().get(i).getVat().getValue(), controllerInvoice
                .getEntries().get(i).getVat().getValue());
            assertEquals(domainInvoice.getEntries().get(i).getAmount(), controllerInvoice
                .getEntries().get(i).getAmount());
        }
    }

    @Test
    void shouldCheckIfDomainInvoicesCollectionHasSameSizeAsControllerInvoiceCollection() {
        // given
        pl.coderstrust.domain.Company company1 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("A").taxIdentificationNumber("1").address("a").build();
        pl.coderstrust.domain.Company company2 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("B").taxIdentificationNumber("2").address("b").build();
        pl.coderstrust.domain.InvoiceEntry invoiceEntry1 = new pl.coderstrust.domain.InvoiceEntry
            .InvoiceEntryBuilder().id(1L).title("C").value(new BigDecimal("12.34"))
            .vat(pl.coderstrust.domain.Vat.VAT_23).amount(2L).build();
        pl.coderstrust.domain.Invoice domainInvoice1 = new pl.coderstrust.domain.Invoice
            .InvoiceBuilder().id(1L).date(LocalDate.of(2018, 4, 9))
            .buyer(company1).seller(company2).entries(Arrays.asList(invoiceEntry1)).build();

        pl.coderstrust.domain.Company company3 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("Ab").taxIdentificationNumber("12").address("ab").build();
        pl.coderstrust.domain.Company company4 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("Bb").taxIdentificationNumber("23").address("bc").build();
        pl.coderstrust.domain.InvoiceEntry invoiceEntry2 = new pl.coderstrust.domain.InvoiceEntry
            .InvoiceEntryBuilder().id(1L).title("Cd").value(new BigDecimal("10.11"))
            .vat(pl.coderstrust.domain.Vat.VAT_8).amount(5L).build();
        pl.coderstrust.domain.Invoice domainInvoice2 = new pl.coderstrust.domain.Invoice
            .InvoiceBuilder().id(2L).date(LocalDate.of(2019, 1, 12))
            .buyer(company3).seller(company4).entries(Arrays.asList(invoiceEntry2)).build();
        Collection<pl.coderstrust.domain.Invoice> domainInvoices = Arrays.asList(domainInvoice1,
            domainInvoice2);

        // when
        Collection<Invoice> controllerInvoices = ModelsMapper.mapInvoices(domainInvoices);

        // then
        assertEquals(controllerInvoices.size(), domainInvoices.size());
    }

}
