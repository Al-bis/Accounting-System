package pl.coderstrust.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApiModel(value = "Invoice", description = "VAT Invoice with entries")
public final class Invoice {

    private final Long id;
    private final LocalDate date;
    private final Company seller;
    private final Company buyer;
    private final List<InvoiceEntry> entries;

    public Invoice(Invoice invoice) {
        this.id = invoice.getId();
        this.date = invoice.getDate();
        this.seller = invoice.getSeller();
        this.buyer = invoice.getBuyer();
        this.entries = invoice.getEntries();
    }

    public Invoice(Long id, Invoice invoice) {
        this.id = id;
        this.date = invoice.getDate();
        this.seller = invoice.getSeller();
        this.buyer = invoice.getBuyer();
        this.entries = invoice.getEntries();
    }

    @JsonCreator
    public Invoice(@JsonProperty("id") Long id, @JsonProperty("date") LocalDate date,
        @JsonProperty("seller") Company seller, @JsonProperty("buyer") Company buyer,
        @JsonProperty("entries") List<InvoiceEntry> entries) {
        this.id = id;
        this.date = date;
        this.seller = seller;
        this.buyer = buyer;
        this.entries = entries;
    }

    public Long getId() {
        return id;
    }

    @ApiModelProperty(value = "Date when invoice was created", example = "2019-02-21")
    public LocalDate getDate() {
        return date;
    }

    public Company getSeller() {
        return seller;
    }

    public Company getBuyer() {
        return buyer;
    }

    public List<InvoiceEntry> getEntries() {
        return entries.stream().collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) object;
        return Objects.equals(id, invoice.id)
            && Objects.equals(date, invoice.date)
            && Objects.equals(seller, invoice.seller)
            && Objects.equals(buyer, invoice.buyer)
            && Objects.equals(entries, invoice.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, seller, buyer, entries);
    }

    @Override
    public String toString() {
        return "Invoice{"
            + "id=" + id
            + ", date=" + date
            + ", seller=" + seller
            + ", buyer=" + buyer
            + ", entries=" + entries
            + '}';
    }

    public static InvoiceBuilder builder() {
        return new InvoiceBuilder();
    }

    public static class InvoiceBuilder {

        private Long id;
        private LocalDate date;
        private Company seller;
        private Company buyer;
        private List<InvoiceEntry> entries;

        public InvoiceBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public InvoiceBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public InvoiceBuilder seller(Company seller) {
            this.seller = seller;
            return this;
        }

        public InvoiceBuilder buyer(Company buyer) {
            this.buyer = buyer;
            return this;
        }

        public InvoiceBuilder entries(List<InvoiceEntry> entries) {
            this.entries = entries;
            return this;
        }

        public Invoice build() {
            if (date == null) {
                throw new IllegalArgumentException("Date cannot be null");
            }
            if (seller == null) {
                throw new IllegalArgumentException("Seller cannot be null");
            }
            if (buyer == null) {
                throw new IllegalArgumentException("Buyer cannot be null");
            }
            if (entries == null) {
                throw new IllegalArgumentException("Entries cannot be null");
            }
            return new Invoice(id, date, seller, buyer, entries);
        }
    }
}
