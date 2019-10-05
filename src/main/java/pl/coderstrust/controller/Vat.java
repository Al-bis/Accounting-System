package pl.coderstrust.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public enum Vat {

    VAT_23(23),
    VAT_8(8),
    VAT_5(5),
    VAT_0(0),
    VAT_ZW(0);

    private BigDecimal value;

    @JsonCreator
    Vat(@JsonProperty("value") long value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}
