package com.example.demo.v1.application.transfer;

import com.example.demo.v1.application.history.HistoryService;
import com.example.demo.v1.application.quote.QuoteService;
import com.example.demo.v1.application.user.UserService;
import com.example.demo.v1.config.exception.CustomException;
import com.example.demo.v1.config.exception.ErrorCode;
import com.example.demo.v1.domain.history.History;
import com.example.demo.v1.domain.quote.Quote;
import com.example.demo.v1.domain.transfer.Transfer;
import com.example.demo.v1.domain.user.IdType;
import com.example.demo.v1.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransferFacade {
    private final TransferService transferService;

    private final HistoryService historyService;

    private final QuoteService quoteService;

    private final UserService userService;

    @Value("${transfer.individual.daily.limit}")
    private BigDecimal INDIVIDUAL_DAILY_LIMIT;

    @Value("${transfer.corporate.daily.limit}")
    private BigDecimal CORPORATE_DAILY_LIMIT;

    @Transactional
    public void createTransfer(String userId, String quoteId) {
        Quote quote = quoteService.getQuote(quoteId);
        User user = userService.getUserById(userId);

        if (!quote.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "본인이 생성한 견적서만 송금 요청이 가능합니다.");
        }

        // 견적서 검증
        validateQuote(quote);

        // 일일한도 검증
        validateDailyLimit(user, quote);

        // 견적서 사용처리
        quoteService.useQuote(quote);

        // 송금 요청
        Transfer transfer = transferService.createTransfer(userId, quote);

        // 송금이력 저장
        historyService.createHistory(new History(transfer));
    }

    private void validateQuote(Quote quote) {
        if (quote.isUsed()) {
            throw new CustomException(ErrorCode.QUOTE_USED, "이미 사용된 견적서입니다.");
        }

        if (quote.isExpired()) {
            throw new CustomException(ErrorCode.QUOTE_EXPIRED, "만료된 견적서입니다.");
        }
    }

    private void validateDailyLimit(User user, Quote quote) {
        BigDecimal totalTransferredUSD = transferService.getTotalTransferredUSDToday(user.getUserId());
        if (user.getIdType() == IdType.REG_NO) {
            if (totalTransferredUSD.add(quote.getTargetUsdAmount()).compareTo(INDIVIDUAL_DAILY_LIMIT) > 0) {
                throw new CustomException(ErrorCode.BAD_REQUEST, "개인 회원은 하루에 $1000 이상 송금할 수 없습니다.");
            }
        } else if (user.getIdType() == IdType.BUSINESS_NO) {
            if (totalTransferredUSD.add(quote.getTargetUsdAmount()).compareTo(CORPORATE_DAILY_LIMIT) > 0) {
                throw new CustomException(ErrorCode.BAD_REQUEST, "기업 회원은 하루에 $5000 이상 송금할 수 없습니다.");
            }
        }
    }
}
