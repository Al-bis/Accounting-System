package pl.coderstrust.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class InvoiceEntry {

    private final Long id;
    private final String title;
    private final BigDecimal value;
    private final Vat vat;

    private InvoiceEntry(Long id, String title, BigDecimal value, Vat vat) {
        if (id < 1) {
            throw new IllegalArgumentException("Given ID cannot be lower then 1");
        }
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException("Given value cannot be negative");
        }
        this.id = id;
        this.title = title;
        this.value = value;
        this.vat = vat;
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

    public Vat getVat() {
        return vat;
    }

    public BigDecimal getValueAfterTax() {
        return value.subtract(value.multiply(vat.getValue().divide(new BigDecimal("100"))))
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
            && vat == that.vat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, value, vat);
    }

    @Override
    public String toString() {
        return "InvoiceEntry{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", value=" + value
            + ", vat=" + vat
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

        public InvoiceEntry build() {
            if (id == null) {
                throw new IllegalArgumentException("Id cannot be null");
            }
            if (title == null) {
                throw new IllegalArgumentException("Title cannot be null");
            }
            if (value == null) {
                throw new IllegalArgumentException("Value cannot be null");
            }
            if (vat == null) {
                throw new IllegalArgumentException("Vat cannot be null");
            }
            return new InvoiceEntry(id, title, value, vat);
        }
    }
}
