package pl.coderstrust.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "Operations on Invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        if (invoiceService == null) {
            throw new IllegalArgumentException("Given service cannot be null");
        }
        this.invoiceService = invoiceService;
    }

    @ApiOperation(value = "Get all invoices", notes = "This function gets all available invoices")
    @GetMapping("/invoices")
    public ResponseEntity<Collection<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(ModelsMapper.mapInvoices(invoiceService.getAllInvoices()));
    }

    @ApiOperation(value = "Get invoices based on given date range",
        notes = "This function gets all invoices which are in range of given date")
    @GetMapping("/invoices/date")
    public ResponseEntity<Collection<Invoice>> getInvoices(
        @DateTimeFormat(iso = ISO.DATE) @RequestParam(value = "from")
        @ApiParam(value = "From date", example = "2019-01-23") LocalDate fromDate,
        @DateTimeFormat(iso = ISO.DATE) @RequestParam(value = "to")
        @ApiParam(value = "To date", example = "2019-01-24") LocalDate toDate) {
        return ResponseEntity
            .ok(ModelsMapper.mapInvoices(invoiceService.getAllInvoices(fromDate, toDate)));
    }

    @ApiOperation(value = "Save Invoice", notes = "This function save given invoice")
    @PostMapping("/invoices")
    public ResponseEntity<Long> saveInvoice(@RequestBody @ApiParam
        (value = "This is invoice which will be saved in system") Invoice invoice) {
        Long invoiceId = invoiceService.saveInvoice(ModelsMapper.mapInvoiceToDomain(invoice));
        return ResponseEntity.ok(invoiceId);
    }

    @ApiOperation(value = "Get invoice", notes = "This function gets invoice based on given ID")
    @GetMapping("/invoices/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable @ApiParam(value = "Invoice ID",
        example = "1") Long id) {
        Invoice invoice = ModelsMapper.mapInvoice(invoiceService.getInvoice(id));
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Delete invoice",
        notes = "This function delete invoice based on given ID")
    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable @ApiParam(value = "Invoice ID",
        example = "1") Long id) {
        if (invoiceService.getInvoice(id) == null) {
            return ResponseEntity.notFound().build();
        }
        invoiceService.deleteInvoice(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
