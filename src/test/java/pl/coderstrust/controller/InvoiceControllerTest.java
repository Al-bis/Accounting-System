package pl.coderstrust.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.coderstrust.controller.Vat.VAT_23;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import pl.coderstrust.domain.InvoiceService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

@ExtendWith(MockitoExtension.class)
class InvoiceControllerTest {

    @Mock
    private InvoiceService service;

    @InjectMocks
    private InvoiceController controller;

    @Test
    void shouldReturnAllInvoices() {
        // given
        pl.coderstrust.domain.Company company1 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("A").taxIdentificationNumber("1").address("a").build();
        pl.coderstrust.domain.Company company2 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("B").taxIdentificationNumber("2").address("b").build();
        pl.coderstrust.domain.InvoiceEntry invoiceEntry1 = new pl.coderstrust.domain.InvoiceEntry
            .InvoiceEntryBuilder().id(1L).title("C").value(new BigDecimal("12.34"))
            .vat(pl.coderstrust.domain.Vat.VAT_23).amount(2L).build();
        pl.coderstrust.domain.InvoiceEntry invoiceEntry2 = new pl.coderstrust.domain.InvoiceEntry
            .InvoiceEntryBuilder().id(1L).title("D").value(new BigDecimal("10.00"))
            .vat(pl.coderstrust.domain.Vat.VAT_23).amount(4L).build();
        pl.coderstrust.domain.Invoice invoice1 = new pl.coderstrust.domain.Invoice
            .InvoiceBuilder().id(1L).date(LocalDate.of(2018, 4, 9))
            .buyer(company1).seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        pl.coderstrust.domain.Invoice invoice2 = new pl.coderstrust.domain.Invoice.InvoiceBuilder()
            .id(2L).date(LocalDate.of(2019, 1, 28)).buyer(company2)
            .seller(company1).entries(Arrays.asList(invoiceEntry2)).build();
        when(service.getAllInvoices()).thenReturn(Arrays.asList(invoice1, invoice2));

        // when
        ResponseEntity<Collection<Invoice>> invoices = controller.getAllInvoices();

        // then
        assertThat(invoices.getBody()).hasSize(2);
        assertThat(invoices.getBody()).containsExactly(ModelsMapper.mapInvoice(invoice1),
            ModelsMapper.mapInvoice(invoice2));
    }

    @Test
    void shouldReturnAllInvoicesInGivenDateRange() {
        // given
        pl.coderstrust.domain.Company company1 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("A").taxIdentificationNumber("1").address("a").build();
        pl.coderstrust.domain.Company company2 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("B").taxIdentificationNumber("2").address("b").build();
        pl.coderstrust.domain.InvoiceEntry invoiceEntry2 = new pl.coderstrust.domain.InvoiceEntry
            .InvoiceEntryBuilder().id(1L).title("D").value(new BigDecimal("10.00"))
            .vat(pl.coderstrust.domain.Vat.VAT_23).amount(4L).build();
        pl.coderstrust.domain.Invoice invoice2 = new pl.coderstrust.domain.Invoice
            .InvoiceBuilder().id(2L).date(LocalDate.of(2019, 1, 28))
            .buyer(company2).seller(company1).entries(Arrays.asList(invoiceEntry2)).build();
        LocalDate date1 = LocalDate.of(2019, 1, 1);
        LocalDate date2 = LocalDate.of(2019, 2, 1);
        when(service.getAllInvoices(date1, date2)).thenReturn(Arrays.asList(invoice2));

        // when
        ResponseEntity<Collection<Invoice>> invoices = controller.getInvoices(date1, date2);

        // then
        assertThat(invoices.getBody()).hasSize(1);
        assertThat(invoices.getBody()).containsExactly(ModelsMapper.mapInvoice(invoice2));
    }

    @Test
    void shouldOnlySaveInvoice() {
        // given
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        InvoiceEntry invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(2L).build();
        Invoice invoice1 = new Invoice.InvoiceBuilder().id(1L)
            .date(LocalDate.of(2018, 4, 9)).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();

        // when
        controller.saveInvoice(invoice1);

        // then
        verify(service, times(1)).saveInvoice(any());
        verify(service, times(0)).getAllInvoices();
        verify(service, times(0)).getAllInvoices(any(), any());
        verify(service, times(0)).getInvoice(anyLong());
        verify(service, times(0)).deleteInvoice(anyLong());
    }

    @Test
    void shouldReturnInvoiceBaseOnGivenId() {
        // given
        pl.coderstrust.domain.Company company1 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("A").taxIdentificationNumber("1").address("a").build();
        pl.coderstrust.domain.Company company2 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("B").taxIdentificationNumber("2").address("b").build();
        pl.coderstrust.domain.InvoiceEntry invoiceEntry1 = new pl.coderstrust.domain.InvoiceEntry
            .InvoiceEntryBuilder().id(1L).title("C").value(new BigDecimal("12.34"))
            .vat(pl.coderstrust.domain.Vat.VAT_23).amount(2L).build();
        pl.coderstrust.domain.Invoice invoice1 = new pl.coderstrust.domain.Invoice.InvoiceBuilder()
            .id(1L).date(LocalDate.of(2018, 4, 9)).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        when(service.getInvoice(1L)).thenReturn(invoice1);

        // when
        ResponseEntity<Invoice> responseEntity = controller.getInvoice(1L);

        // then
        assertEquals(responseEntity.getBody(), ModelsMapper.mapInvoice(invoice1));
    }

    @Test
    void shouldDeleteInvoiceBaseOnGivenId() {
        // given
        pl.coderstrust.domain.Company company1 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("A").taxIdentificationNumber("1").address("a").build();
        pl.coderstrust.domain.Company company2 = new pl.coderstrust.domain.Company.CompanyBuilder()
            .name("B").taxIdentificationNumber("2").address("b").build();
        pl.coderstrust.domain.InvoiceEntry invoiceEntry1 = new pl.coderstrust.domain.InvoiceEntry
            .InvoiceEntryBuilder().id(1L).title("C").value(new BigDecimal("12.34"))
            .vat(pl.coderstrust.domain.Vat.VAT_23).amount(2L).build();
        pl.coderstrust.domain.Invoice invoice1 = new pl.coderstrust.domain.Invoice.InvoiceBuilder()
            .id(1L).date(LocalDate.of(2018, 4, 9)).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        when(service.getInvoice(1L)).thenReturn(invoice1);

        // when
        controller.deleteInvoice(1L);

        // then
        verify(service, times(0)).saveInvoice(any());
        verify(service, times(0)).getAllInvoices();
        verify(service, times(0)).getAllInvoices(any(), any());
        verify(service, times(1)).getInvoice(1L);
        verify(service, times(1)).deleteInvoice(1L);
    }

}
