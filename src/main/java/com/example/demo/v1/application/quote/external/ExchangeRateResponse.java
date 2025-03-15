package com.example.demo.v1.application.quote.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {

    @JsonProperty("code")
    private String code;  // 예: "FRX.KRWJPY"

    @JsonProperty("currencyCode")
    private String currencyCode;  // 예: "JPY"

    @JsonProperty("basePrice")
    private BigDecimal basePrice;  // 예: 907.98

    @JsonProperty("currencyUnit")
    private int currencyUnit;  // 예: 100

    public void scaleBasePrice() {
        if (this.currencyUnit != 0) this.basePrice = this.basePrice.divide(new BigDecimal(currencyUnit));
    }
}
