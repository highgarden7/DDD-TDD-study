package com.example.demo.v1.application.transfer;

import com.example.demo.v1.config.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql(scripts = {"/transfer_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TransferFacadeTest {
    @Autowired
    private TransferFacade transferFacade;

    @Test
    @Order(1)
    @DisplayName("송금 요청 생성 성공")
    void 송금_요청_생성_성공() {
        // Given
        String userId = "test@example.com";
        String quoteId = "test-quote-id";

        // When & Then
        assertDoesNotThrow(() -> transferFacade.createTransfer(userId, quoteId));
    }

    @Test
    @Order(2)
    @DisplayName("견적서 재사용시 실패")
    void 견적서_재사용시_실패() {
        // Given
        String userId = "test@example.com";
        String quoteId = "used-quote-id";

        // When & Then
        assertThrows(CustomException.class, () -> transferFacade.createTransfer(userId, quoteId));
    }

    @Test
    @Order(3)
    @DisplayName("만료된 견적서로 송금요청")
    void 만료된_견적서로_송금요청() {
        // Given
        String userId = "test@example.com";
        String quoteId = "expired-quote-id";

        // When & Then
        assertThrows(CustomException.class, () -> transferFacade.createTransfer(userId, quoteId));
    }

    @Test
    @Order(4)
    @DisplayName("개인 회원이 하루 $1000 이상 송금요청")
    void 개인_회원이_하루_1000USD_이상_송금요청() {
        // Given
        String userId = "test@example.com";
        String quoteId = "individual-quote-id";

        // When & Then
        assertThrows(CustomException.class, () -> transferFacade.createTransfer(userId, quoteId));
    }

    @Test
    @Order(5)
    @DisplayName("법인 회원이 하루 $5000 이상 송금요청")
    void 법인_회원이_하루_5000USD_이상_송금요청() {
        // Given
        String userId = "business@example.com";
        String quoteId = "business-quote-id";

        // When & Then
        assertThrows(CustomException.class, () -> transferFacade.createTransfer(userId, quoteId));
    }
}