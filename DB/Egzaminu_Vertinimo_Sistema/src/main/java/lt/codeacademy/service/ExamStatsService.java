package lt.codeacademy.service;

import lt.codeacademy.entity.ExamStats;
import lt.codeacademy.repository.ExamStatsRepository;

import java.util.List;

public class ExamStatsService {
    private final ExamStatsRepository statsRepository;

    public ExamStatsService() {
        this.statsRepository = new ExamStatsRepository();
    }

    public void createStats(ExamStats examStats) {
        statsRepository.createStats(examStats);
    }

    public void deleteStats(ExamStats examStats) {
        statsRepository.deleteStats(examStats);
    }

}


