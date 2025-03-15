package com.example.demo.v1.application.history;

import com.example.demo.v1.application.user.UserService;
import com.example.demo.v1.domain.history.History;
import com.example.demo.v1.domain.user.User;
import com.example.demo.v1.representation.transfer.dto.TransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HistoryFacade {
    private final HistoryService historyService;

    private final UserService userService;

    public TransferDto.HistoryResponse getHistoryListByUserId(String userId) {
        // 사용자 정보 조회
        User user = userService.getUserById(userId);

        // 오늘 날짜의 송금 내역 조회
        LocalDateTime todayBegin = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
        List<History> todayTransferHistories = historyService.getHistoryListByUserAndDate(userId, todayBegin, todayEnd);

        // 오늘 송금 횟수 및 총 송금 금액(USD) 계산
        int todayTransferCount = todayTransferHistories.size();
        BigDecimal todayTransferUsdAmount = todayTransferHistories.stream()
                .map(History::getUsdAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 전체 송금 이력 조회 및 DTO 변환
        List<History> allHistoryList = historyService.getAllHistoryListByUserId(userId);
        return TransferDto.HistoryResponse.of(
                user.getUserId(),
                user.getName(),
                todayTransferCount,
                todayTransferUsdAmount,
                allHistoryList);
    }
}
