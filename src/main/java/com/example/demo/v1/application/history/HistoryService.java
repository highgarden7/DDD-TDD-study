package com.example.demo.v1.application.history;

import com.example.demo.v1.domain.history.History;
import com.example.demo.v1.infrastructure.history.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public History createHistory(History history) {
        return historyRepository.save(history);
    }

    public List<History> getAllHistoryListByUserId(String userId) {
        return historyRepository.findByUserId(userId);
    }

    public List<History> getHistoryListByUserAndDate(String userId, LocalDateTime begin, LocalDateTime end) {
        return historyRepository.findByUserIdAndRequestedDateBetween(userId, begin, end);
    }
}
