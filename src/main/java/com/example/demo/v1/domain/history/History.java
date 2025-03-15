package com.example.demo.v1.domain.history;

import com.example.demo.v1.domain.quote.CurrencyType;
import com.example.demo.v1.domain.transfer.Transfer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "histories")
@Getter
@NoArgsConstructor
public class History {
    @Id
    private String historyId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private BigDecimal sourceAmount; // 원화 송금액

    @Column(nullable = false)
    private BigDecimal fee; // 송금 수수료

    @Column(nullable = false)
    private BigDecimal usdExchangeRate; // USD 환율

    @Column(nullable = false)
    private BigDecimal usdAmount; // 실제 송금 금액 (보내는통화 -> USD)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType targetCurrency; // 목표 통화

    @Column(nullable = false)
    private BigDecimal exchangeRate; // 환율 (받는 통화 -> 보내는 통화)

    @Column(nullable = false)
    private BigDecimal targetAmount; // 실제 송금 금액

    @Column(nullable = false)
    private LocalDateTime requestedDate; // 송금 요청 시간

    public History(Transfer transfer) {
        this.historyId = UUID.randomUUID().toString();
        this.userId = transfer.getUserId();
        this.sourceAmount = transfer.getQuote().getSendAmount();
        this.fee = transfer.getQuote().getFee();
        this.usdExchangeRate = transfer.getQuote().getUsdExchangeRate();
        this.usdAmount = transfer.getQuote().getTargetUsdAmount();
        this.targetCurrency = transfer.getQuote().getTargetCurrency();
        this.exchangeRate = transfer.getQuote().getExchangeRate();
        this.targetAmount = transfer.getQuote().getTargetAmount();
        this.requestedDate = transfer.getRequestedDate();
    }
}
