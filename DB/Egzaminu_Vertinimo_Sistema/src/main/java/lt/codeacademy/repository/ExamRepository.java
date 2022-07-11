package lt.codeacademy.repository;

import lt.codeacademy.entity.Exam;
import lt.codeacademy.entity.Questions;

import java.util.List;

public class ExamRepository extends AbstractRepository {
    public void createExam(Exam exam) {
        modifyEntity(session -> session.persist(exam));
    }

    public List<Exam> getExams() {
        return getResult(session -> session.createQuery("From Exam", Exam.class).list());
    }

    public void createQuestions(Questions questions) {
        modifyEntity(session -> session.persist(questions));
    }

    public void updateQuestion(Questions question) {
        modifyEntity(session -> session.update(question));
    }

    public void deleteExam(Exam exam) {
        modifyEntity(session -> session.delete(exam));
    }
}
