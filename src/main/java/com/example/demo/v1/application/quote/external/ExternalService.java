package com.example.demo.v1.application.quote.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExternalService {
    private final WebClient webClient;
    @Value("${external.server.url}")
    private String externalUrl;
    @Value("${external.supportedCurrencies}")
    private String supportedCurrencies;

    @Value("${external.exchangeCodes}")
    private String exchangeCodes;

    public Mono<Map<String, ExchangeRateResponse>> getExchangeRates(String targetCurrency) {
        Set<String> supportedCurrencySet = Arrays.stream(supportedCurrencies.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        if (!supportedCurrencySet.contains(targetCurrency)) {
            throw new IllegalArgumentException("지원하지 않는 통화입니다.");
        }

        String url = UriComponentsBuilder.fromUriString(externalUrl)
                .queryParam("codes", exchangeCodes)
                .build()
                .toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ExchangeRateResponse>>() {
                })
                .map(this::convertToMap);
    }

    private Map<String, ExchangeRateResponse> convertToMap(List<ExchangeRateResponse> rates) {
        return rates.stream()
                .collect(Collectors.toMap(ExchangeRateResponse::getCode, rate -> rate));
    }
}
