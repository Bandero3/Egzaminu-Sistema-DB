package lt.codeacademy.service;

import lt.codeacademy.entity.Exam;
import lt.codeacademy.entity.Questions;
import lt.codeacademy.repository.ExamRepository;

import java.util.List;

public class ExamService {
    private final ExamRepository examRepository;

    public ExamService() {
        this.examRepository = new ExamRepository();
    }

    public List<Exam> getExams() {
        return examRepository.getExams();
    }

    public void createExam(Exam exam) {
        examRepository.createExam(exam);
    }

    public void createQuestions(Questions questions) {
        examRepository.createQuestions(questions);
    }

    public void updateQuestion(Questions question) {
        examRepository.updateQuestion(question);
    }

    public void deleteExam(Exam test) {
        examRepository.deleteExam(test);
    }
}
