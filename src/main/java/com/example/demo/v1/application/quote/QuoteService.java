package com.example.demo.v1.application.quote;

import com.example.demo.v1.config.exception.CustomException;
import com.example.demo.v1.config.exception.ErrorCode;
import com.example.demo.v1.domain.quote.CurrencyType;
import com.example.demo.v1.domain.quote.Quote;
import com.example.demo.v1.infrastructure.quote.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public Quote createQuote(BigDecimal sendAmount, CurrencyType targetCurrency, BigDecimal exchangeRate, BigDecimal usdExchangeRate, BigDecimal targetCurrencyUsdExchangeRate, int currencyUnit, String userId) {
        Quote quote = new Quote(sendAmount, targetCurrency, exchangeRate, usdExchangeRate, targetCurrencyUsdExchangeRate, currencyUnit, userId);
        return quoteRepository.save(quote);
    }

    public Quote getQuote(String quoteId) {
        return quoteRepository.findById(quoteId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "견적서를 찾을 수 없습니다."));
    }

    public void useQuote(Quote quote) {
        quote.markUsed();
        quoteRepository.save(quote);
    }
}
