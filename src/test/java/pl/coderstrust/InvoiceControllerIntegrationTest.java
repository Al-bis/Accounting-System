package pl.coderstrust;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import pl.coderstrust.controller.Company;
import pl.coderstrust.controller.Invoice;
import pl.coderstrust.controller.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class InvoiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnDefaultMessageWhenAskingDataBaseWithoutInvoices() throws Exception {
        this.mockMvc.perform(get("/api/invoices"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void shouldReturnDefaultMessageWhenAskingDataBaseWithoutInvoicesByDate()
        throws Exception {
        this.mockMvc.perform(get("/api/invoices/date?from=2019-01-01&to=2019-02-02"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void shouldAddInvoicesAndReturnAllInvoicesAndInvoicesInDateRange() throws Exception {
        Company company1 = Company.builder().name("A").taxIdentificationNumber("1")
            .address("a").build();
        Company company2 = Company.builder().name("B").taxIdentificationNumber("2").address("b")
            .build();
        InvoiceEntry invoiceEntry1 = InvoiceEntry.builder().title("C")
            .value(new BigDecimal("12.34")).vat(pl.coderstrust.controller.Vat.VAT_23)
            .amount(2L).build();
        Invoice invoice1 = Invoice.builder()
            .date(LocalDate.of(2018, 4, 9)).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();

        Company company3 = Company.builder().name("A2").taxIdentificationNumber("12").address("a2")
            .build();
        Company company4 = Company.builder().name("B2").taxIdentificationNumber("22").address("b2")
            .build();
        InvoiceEntry invoiceEntry2 = InvoiceEntry.builder().title("C2")
            .value(new BigDecimal("41.05")).vat(pl.coderstrust.controller.Vat.VAT_8)
            .amount(5L).build();
        InvoiceEntry invoiceEntry3 = InvoiceEntry.builder().title("C3")
            .value(new BigDecimal("2.99")).vat(pl.coderstrust.controller.Vat.VAT_5)
            .amount(8L).build();
        Invoice invoice2 = Invoice.builder().date(LocalDate.of(2019, 7, 18))
            .buyer(company3).seller(company4).entries(Arrays.asList(invoiceEntry2, invoiceEntry3))
            .build();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invoice1)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invoice2)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("5")));
        this.mockMvc.perform(get("/api/invoices"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].date").value("2018-04-09"))
            .andExpect(jsonPath("$[0].seller.name").value("B"))
            .andExpect(jsonPath("$[0].seller.taxIdentificationNumber")
                .value("2"))
            .andExpect(jsonPath("$[0].seller.address").value("b"))
            .andExpect(jsonPath("$[0].buyer.name").value("A"))
            .andExpect(jsonPath("$[0].buyer.taxIdentificationNumber")
                .value("1"))
            .andExpect(jsonPath("$[0].buyer.address").value("a"))
            .andExpect(jsonPath("$[0].entries.[0].title").value("C"))
            .andExpect(jsonPath("$[0].entries.[0].value").value(12.34))
            .andExpect(jsonPath("$[0].entries.[0].vat").value("VAT_23"))
            .andExpect(jsonPath("$[0].entries.[0].amount").value(2))
            .andExpect(jsonPath("$[1].id").value(5))
            .andExpect(jsonPath("$[1].date").value("2019-07-18"))
            .andExpect(jsonPath("$[1].seller.name").value("B2"))
            .andExpect(jsonPath("$[1].seller.taxIdentificationNumber")
                .value("22"))
            .andExpect(jsonPath("$[1].seller.address").value("b2"))
            .andExpect(jsonPath("$[1].buyer.name").value("A2"))
            .andExpect(jsonPath("$[1].buyer.taxIdentificationNumber")
                .value("12"))
            .andExpect(jsonPath("$[1].buyer.address").value("a2"))
            .andExpect(jsonPath("$[1].entries.[0].title").value("C2"))
            .andExpect(jsonPath("$[1].entries.[0].value").value(41.05))
            .andExpect(jsonPath("$[1].entries.[0].vat").value("VAT_8"))
            .andExpect(jsonPath("$[1].entries.[0].amount").value(5))
            .andExpect(jsonPath("$[1].entries.[1].title").value("C3"))
            .andExpect(jsonPath("$[1].entries.[1].value").value(2.99))
            .andExpect(jsonPath("$[1].entries.[1].vat").value("VAT_5"))
            .andExpect(jsonPath("$[1].entries.[1].amount").value(8));
        this.mockMvc.perform(get("/api/invoices/date?from=2019-01-01&to=2019-08-01"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(5))
            .andExpect(jsonPath("$[0].date").value("2019-07-18"))
            .andExpect(jsonPath("$[0].seller.name").value("B2"))
            .andExpect(jsonPath("$[0].seller.taxIdentificationNumber")
                .value("22"))
            .andExpect(jsonPath("$[0].seller.address").value("b2"))
            .andExpect(jsonPath("$[0].buyer.name").value("A2"))
            .andExpect(jsonPath("$[0].buyer.taxIdentificationNumber")
                .value("12"))
            .andExpect(jsonPath("$[0].buyer.address").value("a2"))
            .andExpect(jsonPath("$[0].entries.[0].title").value("C2"))
            .andExpect(jsonPath("$[0].entries.[0].value").value(41.05))
            .andExpect(jsonPath("$[0].entries.[0].vat").value("VAT_8"))
            .andExpect(jsonPath("$[0].entries.[0].amount").value(5))
            .andExpect(jsonPath("$[0].entries.[1].title").value("C3"))
            .andExpect(jsonPath("$[0].entries.[1].value").value(2.99))
            .andExpect(jsonPath("$[0].entries.[1].vat").value("VAT_5"))
            .andExpect(jsonPath("$[0].entries.[1].amount").value(8));
    }

    @Test
    public void shouldReturnInvoiceBasedOnGivenId() throws Exception {
        Company company1 = Company.builder().name("A").taxIdentificationNumber("1").address("a")
            .build();
        Company company2 = Company.builder().name("B").taxIdentificationNumber("2").address("b")
            .build();
        InvoiceEntry invoiceEntry1 = InvoiceEntry.builder().title("C")
            .value(new BigDecimal("12.34")).vat(pl.coderstrust.controller.Vat.VAT_23)
            .amount(2L).build();
        Invoice invoice1 = Invoice.builder()
            .date(LocalDate.of(2018, 4, 9)).buyer(company1)
            .seller(company2).entries(Arrays.asList(invoiceEntry1)).build();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invoice1)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        this.mockMvc.perform(get("/api/invoices/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.date").value("2018-04-09"))
            .andExpect(jsonPath("$.seller.name").value("B"))
            .andExpect(jsonPath("$.seller.taxIdentificationNumber")
                .value("2"))
            .andExpect(jsonPath("$.seller.address").value("b"))
            .andExpect(jsonPath("$.buyer.name").value("A"))
            .andExpect(jsonPath("$.buyer.taxIdentificationNumber")
                .value("1"))
            .andExpect(jsonPath("$.buyer.address").value("a"))
            .andExpect(jsonPath("$.entries.[0].title").value("C"))
            .andExpect(jsonPath("$.entries.[0].value").value(12.34))
            .andExpect(jsonPath("$.entries.[0].vat").value("VAT_23"))
            .andExpect(jsonPath("$.entries.[0].amount").value(2));
        Assertions.assertThrows(NestedServletException.class, () -> {
            this.mockMvc.perform(get("/api/invoices/2"))
                .andExpect(status().isInternalServerError());
        });
    }

    @Test
    public void shouldAddAndThenDeleteInvoice() throws Exception {
        Company company1 = new pl.coderstrust.controller.Company
            .CompanyBuilder().name("A").taxIdentificationNumber("1").address("a").build();
        Company company2 = new pl.coderstrust.controller.Company
            .CompanyBuilder().name("B").taxIdentificationNumber("2").address("b").build();
        InvoiceEntry invoiceEntry1 = new pl.coderstrust.controller
            .InvoiceEntry.InvoiceEntryBuilder().title("C")
            .value(new BigDecimal("12.34")).vat(pl.coderstrust.controller.Vat.VAT_23)
            .amount(2L).build();
        Invoice invoice1 = new pl.coderstrust.controller.Invoice
            .InvoiceBuilder().date(LocalDate.of(2018, 4, 9))
            .buyer(company1).seller(company2).entries(Arrays.asList(invoiceEntry1)).build();

        Company company3 = Company.builder().name("A2").taxIdentificationNumber("12").address("a2")
            .build();
        Company company4 = Company.builder().name("B2").taxIdentificationNumber("22").address("b2")
            .build();
        InvoiceEntry invoiceEntry2 = InvoiceEntry.builder().title("C2")
            .value(new BigDecimal("41.05")).vat(pl.coderstrust.controller.Vat.VAT_8)
            .amount(5L).build();
        InvoiceEntry invoiceEntry3 = InvoiceEntry.builder().title("C3")
            .value(new BigDecimal("2.99")).vat(pl.coderstrust.controller.Vat.VAT_5)
            .amount(8L).build();
        Invoice invoice2 = Invoice.builder().date(LocalDate.of(2019, 7, 18))
            .buyer(company3).seller(company4).entries(Arrays.asList(invoiceEntry2, invoiceEntry3))
            .build();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invoice1)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invoice2)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("5")));
        this.mockMvc.perform(delete("/api/invoices/1"))
            .andExpect(status().isNoContent());
        this.mockMvc.perform(get("/api/invoices"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(5))
            .andExpect(jsonPath("$[0].date").value("2019-07-18"))
            .andExpect(jsonPath("$[0].seller.name").value("B2"))
            .andExpect(jsonPath("$[0].seller.taxIdentificationNumber")
                .value("22"))
            .andExpect(jsonPath("$[0].seller.address").value("b2"))
            .andExpect(jsonPath("$[0].buyer.name").value("A2"))
            .andExpect(jsonPath("$[0].buyer.taxIdentificationNumber")
                .value("12"))
            .andExpect(jsonPath("$[0].buyer.address").value("a2"))
            .andExpect(jsonPath("$[0].entries.[0].title").value("C2"))
            .andExpect(jsonPath("$[0].entries.[0].value").value(41.05))
            .andExpect(jsonPath("$[0].entries.[0].vat").value("VAT_8"))
            .andExpect(jsonPath("$[0].entries.[0].amount").value(5))
            .andExpect(jsonPath("$[0].entries.[1].title").value("C3"))
            .andExpect(jsonPath("$[0].entries.[1].value").value(2.99))
            .andExpect(jsonPath("$[0].entries.[1].vat").value("VAT_5"))
            .andExpect(jsonPath("$[0].entries.[1].amount").value(8));
        Assertions.assertThrows(NestedServletException.class, () -> {
            this.mockMvc.perform(delete("/api/invoices/3"))
                .andExpect(status().isNotFound());
        });
    }

}
