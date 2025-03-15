package com.example.demo.v1.application.transfer;

import com.example.demo.v1.domain.quote.Quote;
import com.example.demo.v1.domain.transfer.Transfer;
import com.example.demo.v1.infrastructure.transfer.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;

    public Transfer createTransfer(String userId, Quote quote) {
        Transfer transfer = new Transfer(userId, quote);

        return transferRepository.save(transfer);
    }

    public BigDecimal getTotalTransferredUSDToday(String userId) {
        LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay();
        List<Transfer> transfers = transferRepository.findByUserIdAndRequestedDateAfter(userId, todayStart);

        return transfers.stream()
                .map(t -> t.getQuote().getTargetUsdAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}