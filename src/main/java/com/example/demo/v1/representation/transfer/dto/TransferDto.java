package com.example.demo.v1.representation.transfer.dto;

import com.example.demo.v1.domain.history.History;
import com.example.demo.v1.domain.quote.CurrencyType;
import com.example.demo.v1.domain.quote.Quote;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TransferDto {
    @Data
    @Builder
    public static class QuoteRequest {
        private BigDecimal amount;
        private String targetCurrency;
    }

    @Getter
    @Builder
    public static class QuoteResponse {
        private int resultCode;
        private String resultMsg;
        private QuoteDetail quote;

        public static QuoteResponse of(Quote quote) {
            return QuoteResponse.builder()
                    .resultCode(200)
                    .resultMsg("OK")
                    .quote(QuoteDetail.of(quote))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class QuoteDetail {
        String quoteId;
        private BigDecimal exchangeRate;
        private BigDecimal targetAmount;
        private LocalDateTime expireTime;

        public static QuoteDetail of(Quote quote) {
            return QuoteDetail.builder()
                    .quoteId(quote.getQuoteId())
                    .exchangeRate(quote.getExchangeRate())
                    .targetAmount(quote.getTargetAmount())
                    .expireTime(quote.getExpiresDate())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class TransferRequest {
        private String quoteId;
    }

    @Getter
    @Builder
    public static class HistoryResponse {
        private int resultCode;
        private String resultMsg;
        private String userId;
        private String name;
        private int todayTransferCount;
        private BigDecimal todayTransferUsdAmount;
        private List<TransferDetail> history;

        public static HistoryResponse of(String userId, String name, int todayTransferCount, BigDecimal todayTransferUsdAmount, List<History> histories) {

            List<TransferDetail> history = histories.stream()
                    .map(TransferDetail::of)
                    .collect(Collectors.toList());

            return HistoryResponse.builder()
                    .resultCode(200)
                    .resultMsg("OK")
                    .userId(userId)
                    .name(name)
                    .todayTransferCount(todayTransferCount)
                    .todayTransferUsdAmount(todayTransferUsdAmount)
                    .history(history)
                    .build();
        }

        @Getter
        @Builder
        public static class TransferDetail {
            private int sourceAmount;
            private BigDecimal fee;
            private BigDecimal usdExchangeRate;
            private BigDecimal usdAmount;
            private CurrencyType targetCurrency;
            private BigDecimal exchangeRate;
            private BigDecimal targetAmount;
            private LocalDateTime requestedDate;

            public static TransferDetail of(History history) {
                return TransferDetail.builder()
                        .sourceAmount(history.getSourceAmount().intValueExact())
                        .fee(history.getFee())
                        .usdExchangeRate(history.getUsdExchangeRate())
                        .usdAmount(history.getUsdAmount())
                        .targetCurrency(history.getTargetCurrency())
                        .exchangeRate(history.getExchangeRate())
                        .targetAmount(history.getTargetAmount())
                        .requestedDate(history.getRequestedDate())
                        .build();
            }
        }
    }
}
