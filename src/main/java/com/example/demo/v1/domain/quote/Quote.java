package com.example.demo.v1.domain.quote;


import com.example.demo.v1.config.exception.CustomException;
import com.example.demo.v1.config.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "quotes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quote {
    @Id
    private String quoteId;

    @Column(nullable = false)
    private String userId;  // 견적서 사용자 ID

    @Column(nullable = false)
    private BigDecimal sendAmount;  // 원화 송금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType targetCurrency;  // 목표 통화

    @Column(nullable = false)
    private BigDecimal exchangeRate;  // 환율 (받는 통화 -> 보내는 통화)

    @Column(nullable = false)
    private BigDecimal fee;  // 송금 수수료

    @Column(nullable = false)
    private BigDecimal targetAmount;  // 실제 송금 금액

    @Column(nullable = false)
    private BigDecimal usdExchangeRate;  // USD 환율

    @Column(nullable = false)
    private BigDecimal targetUsdAmount;  // 실제 송금 금액 (보내는통화 -> USD)

    @Column(nullable = false)
    private LocalDateTime expiresDate;  // 견적 만료 시간 +10분

    @Column(nullable = false)
    private boolean used = false; // 견적서 사용 여부

    public Quote(BigDecimal sendAmount, CurrencyType targetCurrency, BigDecimal exchangeRate, BigDecimal usdExchangeRate, BigDecimal targetCurrencyUsdExchangeRate, int currencyUnit, String userId) {
        validateAmount(sendAmount);

        this.quoteId = UUID.randomUUID().toString();
        this.sendAmount = sendAmount;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
        this.usdExchangeRate = usdExchangeRate;

        this.fee = calculateFee(sendAmount, targetCurrency);

        this.targetAmount = calculateTargetAmount(sendAmount, exchangeRate, this.fee, targetCurrency, currencyUnit);

        if (CurrencyType.USD == targetCurrency) this.targetUsdAmount = targetAmount;
        else
            this.targetUsdAmount = targetAmount.divide(targetCurrencyUsdExchangeRate, CurrencyType.USD.getFractionDigits(), RoundingMode.HALF_EVEN);

        this.expiresDate = LocalDateTime.now().plusMinutes(10);

        this.userId = userId;

        this.used = false;
    }

    private void validateAmount(BigDecimal sendAmount) {
        if (sendAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException(ErrorCode.NEGATIVE_NUMBER, "보내는 금액은 양의정수만 가능.");
        }
    }

    private BigDecimal calculateFee(BigDecimal sendAmount, CurrencyType targetCurrency) {
        if (CurrencyType.USD == targetCurrency) {
            return sendAmount.compareTo(new BigDecimal("1000000")) <= 0 ?
                    sendAmount.multiply(new BigDecimal("0.002")).add(new BigDecimal("1000")) :
                    sendAmount.multiply(new BigDecimal("0.001")).add(new BigDecimal("3000"));
        } else if (CurrencyType.JPY == targetCurrency) {
            return sendAmount.multiply(new BigDecimal("0.005")).add(new BigDecimal("3000"));
        } else {
            throw new CustomException(ErrorCode.NOT_SUPPORTED_CURRENCY, "지원하지 않는 통화입니다.");
        }
    }

    private BigDecimal calculateTargetAmount(BigDecimal sendAmount, BigDecimal exchangeRate, BigDecimal fee, CurrencyType targetCurrency, int currencyUnit) {
        BigDecimal scaledExchangeRate = scaleCurrencyUnit(exchangeRate, currencyUnit);
        BigDecimal targetAmount = sendAmount.subtract(fee).divide(scaledExchangeRate, targetCurrency.getFractionDigits(), RoundingMode.HALF_UP);
        if (targetAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException(ErrorCode.NEGATIVE_NUMBER, "받는 금액은 양의정수만 가능.");
        }
        return targetAmount;
    }

    private BigDecimal scaleCurrencyUnit(BigDecimal exchangeRate, int currencyUnit) {
        return exchangeRate.divide(new BigDecimal(currencyUnit));
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresDate);
    }

    public void markUsed() {
        if (isExpired()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "견적서가 만료되었습니다.");
        }
        if (this.used) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "이미 사용된 견적서입니다.");
        }
        this.used = true;
    }
}
