package com.example.demo.v1.infrastructure.transfer;

import com.example.demo.v1.domain.transfer.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, String> {
    List<Transfer> findByUserIdAndRequestedDateAfter(String userId, LocalDateTime todayStart);
}
