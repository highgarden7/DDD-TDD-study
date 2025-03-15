package com.example.demo.v1.application.transfer;

import com.example.demo.v1.domain.quote.CurrencyType;
import com.example.demo.v1.domain.quote.Quote;
import com.example.demo.v1.domain.transfer.Transfer;
import com.example.demo.v1.infrastructure.quote.QuoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TransferServiceTest {
    @Autowired
    private TransferService transferService;

    @Autowired
    private QuoteRepository quoteRepository;


    @Test
    @Order(1)
    @DisplayName("송금 요청 생성 테스트")
    void createTransfer() {
        // given
        String userId = "test@example.com";
        BigDecimal sendAmount = new BigDecimal("10000");
        CurrencyType targetCurrency = CurrencyType.JPY;
        BigDecimal exchangeRate = new BigDecimal("9.013");
        BigDecimal usdExchangeRate = new BigDecimal("1443.6");
        BigDecimal targetUsdExchangeRate = new BigDecimal("145.23");
        int currencyUnit = 1;

        Quote quote = new Quote(sendAmount, targetCurrency, exchangeRate, usdExchangeRate, targetUsdExchangeRate, currencyUnit, userId);

        quoteRepository.save(quote);

        Optional<Quote> savedQuote = quoteRepository.findById(quote.getQuoteId());

        // when
        Transfer transfer = transferService.createTransfer(userId, quote);

        // then
        assertNotNull(transfer.getTransferId());
        assertEquals(userId, transfer.getUserId());
        assertEquals(transfer.getQuote().getQuoteId(), savedQuote.get().getQuoteId());
        assertNotNull(transfer.getRequestedDate());
    }
}