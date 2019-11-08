package pl.coderstrust.persistence;

import java.math.BigDecimal;

public enum Vat {

    VAT_23(23),
    VAT_8(8),
    VAT_5(5),
    VAT_0(0),
    VAT_ZW(0);

    private BigDecimal value;

    Vat() {
    }

    Vat(long value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}
