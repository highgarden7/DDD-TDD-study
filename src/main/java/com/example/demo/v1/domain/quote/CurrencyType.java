package com.example.demo.v1.domain.quote;

import java.util.Currency;

public enum CurrencyType {
    USD("USD"),
    JPY("JPY");

    private final String code;

    CurrencyType(String code) {
        this.code = code;
    }

    public Currency getCurrency() {
        return Currency.getInstance(this.code);
    }

    public int getFractionDigits() {
        return getCurrency().getDefaultFractionDigits();
    }
}
