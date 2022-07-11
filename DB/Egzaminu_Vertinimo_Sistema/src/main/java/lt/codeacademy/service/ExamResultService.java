package lt.codeacademy.service;

import lt.codeacademy.entity.Exam;
import lt.codeacademy.entity.Result;
import lt.codeacademy.repository.ExamResultRepository;

import java.util.List;

public class ExamResultService {
    private final ExamResultRepository resultRepository;

    public ExamResultService() {
        this.resultRepository = new ExamResultRepository();
    }

    public void createResult(Result examResult) {
        resultRepository.createResult(examResult);
    }

    public void deleteResult(Result examResult) {
        resultRepository.deleteResult(examResult);
    }

}
