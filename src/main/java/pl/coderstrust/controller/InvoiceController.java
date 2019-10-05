package pl.coderstrust.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.domain.InvoiceService;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/api")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public ResponseEntity<Collection<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(ModelsMapper.mapInvoices(invoiceService.getAllInvoices()));
    }

    @GetMapping("/invoices/date")
    public ResponseEntity<Collection<Invoice>> getAllInvoices(
        @DateTimeFormat(iso = ISO.DATE) @RequestParam(value = "from") LocalDate fromDate,
        @DateTimeFormat(iso = ISO.DATE) @RequestParam(value = "to") LocalDate toDate) {
        return ResponseEntity
            .ok(ModelsMapper.mapInvoices(invoiceService.getAllInvoices(fromDate, toDate)));
    }

    @PostMapping("/invoices")
    public ResponseEntity<Long> saveInvoice(@RequestBody Invoice invoice) {
        Long invoiceId = invoiceService.saveInvoice(ModelsMapper.mapInvoiceToDomain(invoice));
        return ResponseEntity.ok(invoiceId);
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        Invoice invoice = ModelsMapper.mapInvoice(invoiceService.getInvoice(id));
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        if (invoiceService.getInvoice(id) == null) {
            return ResponseEntity.notFound().build();
        }
        invoiceService.deleteInvoice(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
