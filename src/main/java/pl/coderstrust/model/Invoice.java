package pl.coderstrust.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Invoice {

    private Long id;
    private LocalDate date;
    private Company seller;
    private Company buyer;
    private List<InvoiceEntry> entries;

    public Invoice(Long id, LocalDate date, Company seller, Company buyer,
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

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Company getSeller() {
        return seller;
    }

    public void setSeller(Company seller) {
        this.seller = seller;
    }

    public Company getBuyer() {
        return buyer;
    }

    public void setBuyer(Company buyer) {
        this.buyer = buyer;
    }

    public List<InvoiceEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<InvoiceEntry> entries) {
        this.entries = entries;
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

    public BigDecimal calculateTotalValue() {
        return entries.stream()
            .map(InvoiceEntry::getValue)
            .reduce(BigDecimal.ZERO, (total, next) -> total.add(next));
    }

    public BigDecimal calculateTotalValueAfterTax() {
        return entries.stream()
            .map(InvoiceEntry::getValueAfterTax)
            .reduce(BigDecimal.ZERO, (total, next) -> total.add(next));
    }
}
