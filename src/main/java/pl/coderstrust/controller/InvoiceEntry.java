package pl.coderstrust.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@ApiModel(value = "InvoiceEntry", description = "Invoice entry which is a position on Invoice")
public final class InvoiceEntry {

    private final Long id;
    private final String title;
    private final BigDecimal value;
    private final Vat vat;
    private final Long amount;

    @JsonCreator
    public InvoiceEntry(@JsonProperty("id") Long id, @JsonProperty("title") String title,
        @JsonProperty("value") BigDecimal value, @JsonProperty("vat") Vat vat,
        @JsonProperty("amount") Long amount) {
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException("Given value cannot be negative");
        }
        if (amount < 1) {
            throw new IllegalArgumentException("Given amount cannot be lower then 1");
        }
        this.id = id;
        this.title = title;
        this.value = value;
        this.vat = vat;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getTotalValue() {
        return value.multiply(new BigDecimal(amount));
    }

    public Vat getVat() {
        return vat;
    }

    public Long getAmount() {
        return amount;
    }

    public BigDecimal getValueAfterTax() {
        return value.subtract(value.multiply(vat.getValue().divide(new BigDecimal("100"))))
            .setScale(2, RoundingMode.CEILING);
    }

    public BigDecimal getTotalValueAfterTax() {
        return value.subtract(value.multiply(vat.getValue().divide(new BigDecimal("100"))))
            .multiply(new BigDecimal(amount))
            .setScale(2, RoundingMode.CEILING);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        InvoiceEntry that = (InvoiceEntry) object;
        return Objects.equals(id, that.id)
            && Objects.equals(title, that.title)
            && Objects.equals(value, that.value)
            && vat == that.vat
            && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, value, vat, amount);
    }

    @Override
    public String toString() {
        return "InvoiceEntry{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", value=" + value
            + ", vat=" + vat
            + ", amount=" + amount
            + '}';
    }

    public static InvoiceEntryBuilder builder() {
        return new InvoiceEntryBuilder();
    }

    public static class InvoiceEntryBuilder {

        private Long id;
        private String title;
        private BigDecimal value;
        private Vat vat;
        private Long amount;

        public InvoiceEntryBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public InvoiceEntryBuilder title(String title) {
            this.title = title;
            return this;
        }

        public InvoiceEntryBuilder value(BigDecimal value) {
            this.value = value;
            return this;
        }

        public InvoiceEntryBuilder vat(Vat vat) {
            this.vat = vat;
            return this;
        }

        public InvoiceEntryBuilder amount(Long amount) {
            this.amount = amount;
            return this;
        }

        public InvoiceEntry build() {
            if (title == null) {
                throw new IllegalArgumentException("Title cannot be null");
            }
            if (value == null) {
                throw new IllegalArgumentException("Value cannot be null");
            }
            if (vat == null) {
                throw new IllegalArgumentException("Vat cannot be null");
            }
            if (amount == null) {
                throw new IllegalArgumentException("Amount cannot be null");
            }
            return new InvoiceEntry(id, title, value, vat, amount);
        }
    }
}
