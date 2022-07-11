package lt.codeacademy.repository;

import lt.codeacademy.entity.ExamStats;

import java.util.List;


public class ExamStatsRepository extends AbstractRepository {
    public void createStats(ExamStats examStats) {
        modifyEntity(session -> session.persist(examStats));
    }

    public void deleteStats(ExamStats examStats) {
        modifyEntity(session -> session.delete(examStats));
    }

}

