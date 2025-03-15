package com.example.demo.v1.application.quote;

import com.example.demo.v1.config.exception.CustomException;
import com.example.demo.v1.domain.quote.CurrencyType;
import com.example.demo.v1.domain.quote.Quote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class QuoteServiceTest {
    @Autowired
    private QuoteService quoteService;

    @Test
    @Order(1)
    @DisplayName("견적서 생성 테스트 성공 USD 100만원 이하")
    void 견적서_생성_성공_USD_100만원_이하() {
        // Given
        String userId = "test@example.com";
        BigDecimal sendAmount = new BigDecimal("100000");
        CurrencyType targetCurrency = CurrencyType.USD;
        BigDecimal exchangeRate = new BigDecimal("1443.6");
        BigDecimal usdExchangeRate = new BigDecimal("1443.6");
        BigDecimal targetUsdExchangeRate = new BigDecimal("1443.6");
        int currencyUnit = 1;

        // When
        Quote savedQuote = quoteService.createQuote(sendAmount, targetCurrency, exchangeRate, usdExchangeRate, targetUsdExchangeRate, currencyUnit, userId);

        // Then
        assertThat(savedQuote).isNotNull();
        printQuote(savedQuote);
        assertThat(savedQuote.getSendAmount()).isEqualTo(new BigDecimal("100000"));
        assertThat(savedQuote.getTargetCurrency()).isEqualTo(CurrencyType.USD);
    }

    @Test
    @Order(1)
    @DisplayName("견적서 생성 테스트 성공 USD 100만원 초과")
    void 견적서_생성_성공_USD_100만원_초과() {
        // Given
        String userId = "test@example.com";
        BigDecimal sendAmount = new BigDecimal("10000000");
        CurrencyType targetCurrency = CurrencyType.USD;
        BigDecimal exchangeRate = new BigDecimal("1443.6");
        BigDecimal usdExchangeRate = new BigDecimal("1443.6");
        BigDecimal targetUsdExchangeRate = new BigDecimal("1443.6");
        int currencyUnit = 1;

        // When
        Quote savedQuote = quoteService.createQuote(sendAmount, targetCurrency, exchangeRate, usdExchangeRate, targetUsdExchangeRate, currencyUnit, userId);

        // Then
        assertThat(savedQuote).isNotNull();
        printQuote(savedQuote);
        assertThat(savedQuote.getSendAmount()).isEqualTo(new BigDecimal("10000000"));
        assertThat(savedQuote.getTargetCurrency()).isEqualTo(CurrencyType.USD);
    }

    @Test
    @Order(1)
    @DisplayName("견적서 생성 테스트 성공 JPY")
    void 견적서_생성_성공_JPY() {
        // Given
        String userId = "test@example.com";
        BigDecimal sendAmount = new BigDecimal("10000");
        CurrencyType targetCurrency = CurrencyType.JPY;
        BigDecimal exchangeRate = new BigDecimal("9.013");
        BigDecimal usdExchangeRate = new BigDecimal("1443.6");
        BigDecimal targetUsdExchangeRate = new BigDecimal("145.23");
        int currencyUnit = 100;

        // When
        Quote savedQuote = quoteService.createQuote(sendAmount, targetCurrency, exchangeRate, usdExchangeRate, targetUsdExchangeRate, currencyUnit, userId);

        // Then
        assertThat(savedQuote).isNotNull();
        printQuote(savedQuote);
        assertThat(savedQuote.getSendAmount()).isEqualTo(new BigDecimal("10000"));
        assertThat(savedQuote.getTargetCurrency()).isEqualTo(CurrencyType.JPY);
    }
    @Test
    @Order(2)
    @DisplayName("견적서 생성 실패 테스트 (보내는금액 음수)")
    void 견적서_생성_실패_보내는금액이_음수() {
        // Given
        String userId = "test@example.com";
        BigDecimal sendAmount = new BigDecimal("-10000");
        CurrencyType targetCurrency = CurrencyType.JPY;
        BigDecimal exchangeRate = new BigDecimal("9.013");
        BigDecimal usdExchangeRate = new BigDecimal("1443.6");
        BigDecimal targetUsdExchangeRate = new BigDecimal("145.23");
        int currencyUnit = 100;

        // When & Then
        assertThrows(CustomException.class, () -> quoteService.createQuote(sendAmount, targetCurrency, exchangeRate, usdExchangeRate, targetUsdExchangeRate, currencyUnit, userId));
    }

    @Test
    @Order(2)
    @DisplayName("견적서 생성 실패 테스트 (받는금액 음수)")
    void 견적서_생성_실패_받는금액이_음수() {
        // Given
        String userId = "test@example.com";
        BigDecimal sendAmount = new BigDecimal("3000");
        CurrencyType targetCurrency = CurrencyType.JPY;
        BigDecimal exchangeRate = new BigDecimal("9.013");
        BigDecimal usdExchangeRate = new BigDecimal("1443.6");
        BigDecimal targetUsdExchangeRate = new BigDecimal("145.23");
        int currencyUnit = 1;

        // When & Then
        assertThrows(CustomException.class, () -> quoteService.createQuote(sendAmount, targetCurrency, exchangeRate, usdExchangeRate, targetUsdExchangeRate, currencyUnit, userId));
    }

    private void printQuote(Quote quote) {
        System.out.println("UserId : " + quote.getUserId());
        System.out.println("Target Currency : " + quote.getTargetCurrency());
        System.out.println("Fee : " + quote.getFee());
        System.out.println("Send Amount : " + quote.getSendAmount());
        System.out.println("Target Amount : " + quote.getTargetAmount());
        System.out.println("Exchange Rate : " + quote.getExchangeRate());
        System.out.println("Usd Exchange Rate : " + quote.getUsdExchangeRate());
        System.out.println("Target Usd Amount : " + quote.getTargetUsdAmount());
    }
}