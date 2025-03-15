package com.example.demo.v1.domain.transfer;

import com.example.demo.v1.domain.quote.Quote;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Table(name = "transfers")
@NoArgsConstructor
@Entity
public class Transfer {
    @Id
    private String transferId;  // 송금 요청 ID

    @Column(nullable = false)
    private String userId;  // 송금 요청한 사용자 ID

    @OneToOne(fetch = FetchType.LAZY)  // Quote와 연결 (1:1 관계)
    @JoinColumn(name = "quote_id", nullable = false)
    private Quote quote;

    @Column(nullable = false)
    private LocalDateTime requestedDate;  // 송금 요청 시간

    public Transfer(String userId, Quote quote) {
        this.transferId = UUID.randomUUID().toString();
        this.userId = userId;
        this.quote = quote;
        this.requestedDate = LocalDateTime.now();
    }
}
