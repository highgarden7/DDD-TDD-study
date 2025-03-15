package com.example.demo.v1.application.quote;

import com.example.demo.v1.application.quote.external.ExchangeRateResponse;
import com.example.demo.v1.application.quote.external.ExternalService;
import com.example.demo.v1.config.exception.CustomException;
import com.example.demo.v1.config.exception.ErrorCode;
import com.example.demo.v1.domain.quote.CurrencyType;
import com.example.demo.v1.representation.transfer.dto.TransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class QuoteFacade {
    private final QuoteService quoteService;

    private final ExternalService externalService;

    @Transactional
    public TransferDto.QuoteResponse createQuote(BigDecimal sendAmount, String targetCurrency, String userId) {
        // 외부 API 호출
        Map<String, ExchangeRateResponse> rates = externalService.getExchangeRates(targetCurrency).block();

        if (rates == null) {
            throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
        }

        // 보내는 통화 기준 환율 정보
        ExchangeRateResponse exchangeRate = getExchangeRate(rates, targetCurrency);

        // 원 달러 환율
        ExchangeRateResponse usdExchangeRate = getUsdExchangeRate(rates);

        CurrencyType currencyType = CurrencyType.valueOf(targetCurrency);

        if (currencyType.equals(CurrencyType.USD)) {
            return TransferDto.QuoteResponse.of(quoteService.createQuote(sendAmount, currencyType, exchangeRate.getBasePrice(), exchangeRate.getBasePrice(), exchangeRate.getBasePrice(), exchangeRate.getCurrencyUnit(), userId));
        } else {
            ExchangeRateResponse targetCurrencyUsdRate = getTargetCurrencyUsdRate(rates, targetCurrency);
            return TransferDto.QuoteResponse.of(quoteService.createQuote(sendAmount, currencyType, exchangeRate.getBasePrice(), usdExchangeRate.getBasePrice(), targetCurrencyUsdRate.getBasePrice(), exchangeRate.getCurrencyUnit(), userId));
        }
    }

    private ExchangeRateResponse getExchangeRate(Map<String, ExchangeRateResponse> rates, String targetCurrency) {
        String key = "FRX.KRW" + targetCurrency;
        return rates.getOrDefault(key, null);
    }

    private ExchangeRateResponse getUsdExchangeRate(Map<String, ExchangeRateResponse> rates) {
        String key = "FRX.KRWUSD";
        return rates.getOrDefault(key, null);
    }

    private ExchangeRateResponse getTargetCurrencyUsdRate(Map<String, ExchangeRateResponse> rates, String targetCurrency) {
        String key = "FRX." + targetCurrency + "USD";
        return rates.getOrDefault(key, null);
    }
}
