package com.example.demo.v1.application.quote.external;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExternalServiceTest {
    @Autowired
    private ExternalService externalService;

    @Test
    @Order(1)
    @DisplayName("외부 API 연동 테스트 성공")
    void 외부_API_연동_성공() {
        // When
        Mono<Map<String, ExchangeRateResponse>> exchangeRatesMono = externalService.getExchangeRates("JPY");
        Map<String, ExchangeRateResponse> exchangeRates = exchangeRatesMono.block();

        // Then
        assertThat(exchangeRates).isNotEmpty();
        exchangeRates.forEach((key, value) -> {
            System.out.println("Code : " + value.getCode());
            System.out.println("CurrencyCode : " + value.getCurrencyCode());
            System.out.println("BasePrice : " + value.getBasePrice());
            System.out.println("CurrencyUnit : " + value.getCurrencyUnit());
        });
    }

}