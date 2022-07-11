package lt.codeacademy.controller;

import lt.codeacademy.entity.*;
import lt.codeacademy.service.ExamResultService;
import lt.codeacademy.service.ExamService;
import lt.codeacademy.service.ExamStatsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Student {

    private final ExamService examService = new ExamService();
    private final ExamResultService resultService = new ExamResultService();
    private final ExamStatsService statsService = new ExamStatsService();
    public static List<Exam> exams = new ArrayList<>();
    public static List<Result> examsResults = new ArrayList<>();
    public static List<ExamStats> examStats = new ArrayList<>();

    public void studentAction(User user, Login login) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String action;


        do {
            studentMenu();
            action = scanner.nextLine();
            switch (action) {
                case "1" -> takeExam(scanner, user);
                case "2" -> checkAverage(user);
                case "3" -> {
                    System.out.println("Atsijungete");
                    login.loggedIn = false;
                    login.selectAction(scanner, login);
                }
                default -> System.out.println("Tokio veiksmo nera");
            }
        } while (!action.equals("3"));
    }

    public void studentMenu() {
        System.out.println("""
                [1]. Laikyti Egzamina
                [2]. Žiureti vidurki
                [3]. Atsijungti
                """);
    }

    public void checkAverage(User user) {
        examsResults = user.getResultList();
        if (examsResults.isEmpty()) {
            System.out.println("Nera jokiu pazymiu");
            return;
        }
        for (Result result : examsResults) {
            System.out.printf("%s - %d\n", result.getExam().getName(), result.getGrade());
        }
    }


    public void takeExam(Scanner scanner, User user) {
        exams = examService.getExams();
        if (exams.isEmpty()) {
            System.out.println("Nera jokiu egzaminu");
            return;
        }
        double correctAnswers = 0;

        int answeredA = 0;
        int answeredB = 0;
        int answeredC = 0;
        int answeredIncorrectly = 0;

        exams.stream().map(Exam::getName).forEach(System.out::println);
        Exam exam = getExamName(scanner, user);
        if (exam == null) {
            return;
        }
        int questionNumb = 0;
        double numberOfAnswers = exam.getQuestions().size();
        for (Questions question : exam.getQuestions()) {
            questionNumb++;
            System.out.printf("%d. %s |a. %s | b. %s | c. %s\n", questionNumb, question.getQuestion(), question.getAnswerA(), question.getAnswerB(), question.getAnswerC());
            String answer = getCorrectAnswer(scanner);

            switch (answer) {
                case "a" -> answeredA++;
                case "b" -> answeredB++;
                case "c" -> answeredC++;
            }

            if (question.getCorrectAnswer().equals(answer)) {
                correctAnswers++;
            } else {
                answeredIncorrectly++;
            }
        }

        int grade = (int) Math.round((correctAnswers / numberOfAnswers) * 10);
        System.out.println("Egzaminas baigtas");
        System.out.printf("Jusu pazymis yra: %s\n", grade);

        Result examResult = new Result(grade);
//
        examsResults.add(examResult);
        examResult.setUser(user);
        examResult.setExam(exam);
        resultService.createResult(examResult);

        examStats = exam.getExamStats();
        ExamStats existingExamStats = examStats.stream()
                .filter(n -> n.getExam().getId() == exam.getId())
                .findFirst()
                .orElse(null);

        if (existingExamStats != null) {
            ExamStats examStatsNew = new ExamStats(answeredA + existingExamStats.getAnsweredA(), answeredB + existingExamStats.getAnsweredB(), answeredC + existingExamStats.getAnsweredC(), (int) correctAnswers + existingExamStats.getAnsweredCorrectly(), answeredIncorrectly + existingExamStats.getAnsweredIncorrectly());

            examStats.remove(existingExamStats);
            statsService.deleteStats(existingExamStats);
            examStatsNew.setExam(exam);
            statsService.createStats(examStatsNew);
            examStats.add(examStatsNew);
        } else {
            ExamStats examStatsNew = new ExamStats(answeredA, answeredB, answeredC, (int) correctAnswers, answeredIncorrectly);
            examStats.add(examStatsNew);
            examStatsNew.setExam(exam);
            statsService.createStats(examStatsNew);
        }


    }

    public String getCorrectAnswer(Scanner scanner) {
        while (true) {
            String answer = scanner.nextLine();
            switch (answer) {
                case "a" -> {
                    return answer;
                }
                case "b" -> {
                    return answer;
                }
                case "c" -> {
                    return answer;
                }
                default -> System.out.println("Tokio atsakymo nera");
            }

        }
    }

    private Exam getExamName(Scanner scanner, User user) {
        System.out.println("Parašykite pavadinima egzamino kuri norite laikyti");
        String examName = scanner.nextLine();
        exams = examService.getExams();
        Exam existingExam = exams.stream()
                .filter(n -> n.getName().equals(examName))
                .findFirst()
                .orElse(null);
        if (existingExam == null) {
            System.out.printf("Egzamino pavadinimu %s nera\n", examName);
            return null;
        }

        examsResults = user.getResultList();
        Result existingExamResult = examsResults.stream()
                .filter(n -> n.getExam().getId() == existingExam.getId())
                .findFirst()
                .orElse(null);

        if (existingExamResult != null) {
            System.out.printf("Perlaikote %s egzamina\n", examName);
            examsResults.remove(existingExamResult);
            resultService.deleteResult(existingExamResult);
            return existingExam;
        }
        System.out.printf("Laikote %s egzamina\n", examName);
        return existingExam;
    }

}
