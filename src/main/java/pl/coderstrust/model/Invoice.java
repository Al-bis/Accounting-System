package pl.coderstrust.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    private Invoice(Long id, LocalDate date, Company seller, Company buyer,
        List<InvoiceEntry> entries) {
        if (id < 1) {
            throw new IllegalArgumentException("Given ID cannot be lower then 1");
        }
        this.id = id;
        this.date = date;
        this.seller = seller;
        this.buyer = buyer;
        this.entries = entries;
    }

    public Long getId() {
        return id;
    }

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
        return List.copyOf(entries);
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
            if (id == null) {
                throw new IllegalArgumentException("Id cannot be null");
            }
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
