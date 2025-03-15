package com.example.demo.v1.application.history;

import com.example.demo.v1.representation.transfer.dto.TransferDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Sql(scripts = {"/history_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class HistoryFacadeTest {
    @Autowired
    private HistoryFacade historyFacade;

    @Test
    @Order(1)
    @DisplayName("개인 사용자 이력 조회 성공")
    void 개인_사용자_이력조회_성공() {
        // Given
        String userId = "test@example.com";

        // When
        TransferDto.HistoryResponse historyList = historyFacade.getHistoryListByUserId(userId);

        // Then
        assertNotNull(historyList);
        assertEquals(userId, historyList.getUserId());
        assertEquals(2, historyList.getHistory().size());
    }

    @Test
    @Order(2)
    @DisplayName("법인 사용자 이력 조회 성공")
    void 법인_사용자_이력조회_성공() {
        // Given
        String userId = "business@example.com";

        // When
        TransferDto.HistoryResponse historyList = historyFacade.getHistoryListByUserId(userId);

        // Then
        assertNotNull(historyList);
        assertEquals(userId, historyList.getUserId());
        assertEquals(1, historyList.getTodayTransferCount());

    }
}