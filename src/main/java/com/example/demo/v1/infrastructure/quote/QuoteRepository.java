package com.example.demo.v1.infrastructure.quote;

import com.example.demo.v1.domain.quote.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, String> {
}
