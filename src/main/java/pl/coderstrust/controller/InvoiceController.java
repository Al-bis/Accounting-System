package pl.coderstrust.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.domain.Invoice;
import pl.coderstrust.domain.InvoiceService;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public Collection<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/byDate/{fromDate}_{toDate}")
    public Collection<Invoice> getAllInvoices(
        @DateTimeFormat(iso = ISO.DATE) @PathVariable LocalDate fromDate,
        @DateTimeFormat(iso = ISO.DATE) @PathVariable LocalDate toDate) {
        return invoiceService.getAllInvoices(fromDate, toDate);
    }

    @PostMapping
    public void saveInvoice(@RequestBody Invoice invoice) {
        invoiceService.saveInvoice(invoice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoice(id);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        if (invoiceService.getInvoice(id) == null) {
            return ResponseEntity.notFound().build();
        }
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().build();
    }

}
