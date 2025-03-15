package com.example.demo.v1.application.quote;

import com.example.demo.v1.representation.transfer.dto.TransferDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class QuoteFacadeTest {
    @Autowired
    private QuoteFacade quoteFacade;

    @Test
    @Order(1)
    @DisplayName("견적서 생성 테스트 성공 USD 100만원 이하")
    void 견적서_생성_성공_100만원_이하_USD() {
        // Given
        String userId = "test@example.com";
        String targetCurrency = "USD";
        BigDecimal sendAmount = new BigDecimal("100000");

        // When
        TransferDto.QuoteResponse response = quoteFacade.createQuote(sendAmount, targetCurrency, userId);

        // Then
        assertNotNull(response);
    }

    @Test
    @Order(2)
    @DisplayName("견적서 생성 테스트 성공 USD 100만원 초과")
    void 견적서_생성_성공_100만원_초과_USD() {
        // Given
        String userId = "test@example.com";
        String targetCurrency = "USD";
        BigDecimal sendAmount = new BigDecimal("10000000");

        // When
        TransferDto.QuoteResponse response = quoteFacade.createQuote(sendAmount, targetCurrency, userId);

        // Then
        assertNotNull(response);
    }

    @Test
    @Order(2)
    @DisplayName("견적서 생성 테스트 성공 JPY")
    void 견적서_생성_성공_JPY() {
        // Given
        String userId = "test@example.com";
        String targetCurrency = "JPY";
        BigDecimal sendAmount = new BigDecimal("100000");

        // When
        TransferDto.QuoteResponse response = quoteFacade.createQuote(sendAmount, targetCurrency, userId);

        // Then
        assertNotNull(response);
    }

    @Test
    @Order(3)
    @DisplayName("견적서 생성 테스트 실패 환율 정보 없음")
    void 견적서_생성_실패_환율정보_없음() {
        // Given
        String userId = "test@example.com";
        String targetCurrency = "KRW";
        BigDecimal sendAmount = new BigDecimal("100000");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> quoteFacade.createQuote(sendAmount, targetCurrency, userId));
    }
}