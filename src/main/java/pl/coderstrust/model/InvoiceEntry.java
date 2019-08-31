package pl.coderstrust.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class InvoiceEntry {

    private Long id;
    private String title;
    private BigDecimal value;
    private Vat vat;

    public InvoiceEntry(Long id, String title, BigDecimal value, Vat vat) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Vat getVat() {
        return vat;
    }

    public void setVat(Vat vat) {
        this.vat = vat;
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
}
