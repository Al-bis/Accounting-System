package pl.coderstrust.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.persistence.InvoiceRepository;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository database;

    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    void shouldAddInvoicesToDbAndThenReturnAllInvoicesFromDb() {
        // when
        invoiceService.saveInvoice(any(Invoice.class));
        invoiceService.saveInvoice(any(Invoice.class));
        invoiceService.saveInvoice(any(Invoice.class));
        invoiceService.getAllInvoices();

        // then
        verify(database, times(3)).saveInvoice(any());
        verify(database, times(1)).getAllInvoices();
        verify(database, times(0)).getInvoice(anyLong());
        verify(database, times(0)).deleteInvoice(anyLong());
    }

    @Test
    void shouldAddInvoicesToDbAndThenReturnAllInvoicesFromDbInGivenRange() {
        // when
        invoiceService.saveInvoice(any(Invoice.class));
        invoiceService.saveInvoice(any(Invoice.class));
        invoiceService.saveInvoice(any(Invoice.class));
        invoiceService.getAllInvoices(any(LocalDate.class), any(LocalDate.class));

        // then
        verify(database, times(3)).saveInvoice(any());
        verify(database, times(0)).getAllInvoices();
        verify(database, times(1)).getInvoices(any(), any());
        verify(database, times(0)).getInvoice(anyLong());
        verify(database, times(0)).deleteInvoice(anyLong());
    }

    @Test
    void shouldAddInvoiceToDbAndThenDeleteThisInvoiceFromDb() {
        // when
        invoiceService.saveInvoice(any(Invoice.class));
        invoiceService.deleteInvoice(anyLong());

        // then
        verify(database, times(1)).saveInvoice(any());
        verify(database, times(0)).getAllInvoices();
        verify(database, times(0)).getInvoice(anyLong());
        verify(database, times(1)).deleteInvoice(anyLong());
    }

    @Test
    void shouldAddInvoiceToDbAndThenReturnThisInvoiceFromDb() {
        // when
        invoiceService.saveInvoice(any(Invoice.class));
        invoiceService.getInvoice(anyLong());

        // then
        verify(database, times(1)).saveInvoice(any());
        verify(database, times(0)).getAllInvoices();
        verify(database, times(1)).getInvoice(anyLong());
        verify(database, times(0)).deleteInvoice(anyLong());
    }
}
