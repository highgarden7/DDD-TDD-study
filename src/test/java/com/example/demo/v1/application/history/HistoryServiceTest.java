package com.example.demo.v1.application.history;

import com.example.demo.v1.domain.history.History;
import com.example.demo.v1.domain.quote.CurrencyType;
import com.example.demo.v1.domain.quote.Quote;
import com.example.demo.v1.domain.transfer.Transfer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class HistoryServiceTest {
    @Autowired
    private HistoryService historyService;

    @Test
    @Order(1)
    @DisplayName("히스토리 생성 테스트")
    void 히스토리_생성_성공() {
        // given
        String userId = "test@example.com";
        Transfer transfer = getNewTransfer(userId);
        History history = new History(transfer);

        // when
        History savedHistory = historyService.createHistory(history);

        // then
        assertNotNull(savedHistory.getHistoryId());
        assertEquals(userId, savedHistory.getUserId());
        assertNotNull(savedHistory.getRequestedDate());
    }

    private static Transfer getNewTransfer(String userId) {
        BigDecimal sendAmount = new BigDecimal("10000");
        CurrencyType targetCurrency = CurrencyType.JPY;
        BigDecimal exchangeRate = new BigDecimal("9.013");
        BigDecimal usdExchangeRate = new BigDecimal("1443.6");
        BigDecimal targetUsdExchangeRate = new BigDecimal("145.23");
        int currencyUnit = 1;

        Quote quote = new Quote(sendAmount, targetCurrency, exchangeRate, usdExchangeRate, targetUsdExchangeRate, currencyUnit, userId);
        Transfer transfer = new Transfer(userId, quote);
        return transfer;
    }

}