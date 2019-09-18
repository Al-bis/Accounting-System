package pl.coderstrust.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.coderstrust.domain.Vat.VAT_23;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import pl.coderstrust.domain.Company;
import pl.coderstrust.domain.Invoice;
import pl.coderstrust.domain.InvoiceEntry;
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
    public void shouldReturnAllInvoices() {
        // given
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        InvoiceEntry invoiceEntry1 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("C")
            .value(new BigDecimal("12.34")).vat(VAT_23).amount(2L).build();
        InvoiceEntry invoiceEntry2 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("D")
            .value(new BigDecimal("10.00")).vat(VAT_23).amount(4L).build();
        Invoice invoice1 = new Invoice.InvoiceBuilder().id(1L)
            .date(LocalDate.of(2018, 4, 9)).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();
        Invoice invoice2 = new Invoice.InvoiceBuilder().id(2L)
            .date(LocalDate.of(2019, 1, 28)).buyer(company2)
            .seller(company1).entries(Arrays.asList(invoiceEntry2)).build();
        when(service.getAllInvoices()).thenReturn(Arrays.asList(invoice1, invoice2));

        // when
        Collection<Invoice> invoices = controller.getAllInvoices();

        // then
        assertThat(invoices).hasSize(2);
        assertThat(invoices).containsExactly(invoice1, invoice2);
    }

    @Test
    public void shouldReturnAllInvoicesInGivenDateRange() {
        // given
        Company company1 = new Company.CompanyBuilder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = new Company.CompanyBuilder().name("B").taxIdentificationNumber("2")
            .address("b").build();
        InvoiceEntry invoiceEntry2 = new InvoiceEntry.InvoiceEntryBuilder().id(1L).title("D")
            .value(new BigDecimal("10.00")).vat(VAT_23).amount(4L).build();
        Invoice invoice2 = new Invoice.InvoiceBuilder().id(2L)
            .date(LocalDate.of(2019, 1, 28)).buyer(company2)
            .seller(company1).entries(Arrays.asList(invoiceEntry2)).build();
        LocalDate date1 = LocalDate.of(2019, 1, 1);
        LocalDate date2 = LocalDate.of(2019, 2, 1);
        when(service.getAllInvoices(date1, date2)).thenReturn(Arrays.asList(invoice2));

        // when
        Collection<Invoice> invoices = controller
            .getAllInvoices(date1, date2);

        // then
        assertThat(invoices).hasSize(1);
        assertThat(invoices).containsExactly(invoice2);
    }

    @Test
    public void shouldOnlySaveInvoice() {
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
        verify(service, times(1)).saveInvoice(invoice1);
        verify(service, times(0)).getAllInvoices();
        verify(service, times(0)).getAllInvoices(any(), any());
        verify(service, times(0)).getInvoice(anyLong());
        verify(service, times(0)).deleteInvoice(anyLong());
    }

    @Test
    public void shouldReturnInvoiceBaseOnGivenId() {
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
        when(service.getInvoice(1L)).thenReturn(invoice1);

        // when
        ResponseEntity<Invoice> responseEntity = controller.getInvoice(1L);

        // then
        assertEquals(responseEntity.getBody(), invoice1);
    }

    @Test
    public void shouldDeleteInvoiceBaseOnGivenId() {
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
