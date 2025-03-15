package com.example.demo.v1.infrastructure.history;

import com.example.demo.v1.domain.history.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUserId(String userId);

    List<History> findByUserIdAndRequestedDateBetween(String userId, LocalDateTime begin, LocalDateTime end);
}
