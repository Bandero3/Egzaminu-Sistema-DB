package lt.codeacademy.repository;

import lt.codeacademy.entity.Result;

import java.util.List;


public class ExamResultRepository extends AbstractRepository {
    public void createResult(Result examResult) {
        modifyEntity(session -> session.persist(examResult));
    }


    public void deleteResult(Result examResult) {
        modifyEntity(session -> session.delete(examResult));
    }

}
