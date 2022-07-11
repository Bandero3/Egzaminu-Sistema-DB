package lt.codeacademy.controller;

import lt.codeacademy.data.UserType;
import lt.codeacademy.entity.*;
import lt.codeacademy.service.ExamResultService;
import lt.codeacademy.service.ExamService;
import lt.codeacademy.service.ExamStatsService;
import lt.codeacademy.service.UserService;

import java.io.IOException;
import java.util.*;


public class Teacher {
    public static List<Exam> exams = new ArrayList<>();
    public static List<ExamStats> examStats = new ArrayList<>();
    public static List<Questions> questions = new ArrayList<>();
    private final ExamService examService = new ExamService();
    private final ExamResultService resultService = new ExamResultService();
    private final ExamStatsService statsService = new ExamStatsService();
    private final UserService userService = new UserService();

    public void teacherAction(Login login) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String action;

        do {
            teacherMenu();
            action = scanner.nextLine();
            switch (action) {
                case "1" -> createExam(scanner);
                case "2" -> editExam(scanner);
                case "3" -> deleteExam(scanner);
                case "4" -> checkAverage(scanner);
                case "5" -> checkExams(scanner);
                case "6" -> {
                    System.out.println("Atsijungete");
                    login.loggedIn = false;
                    login.selectAction(scanner, login);
                }
                default -> System.out.println("Tokio veiksmo nera");
            }
        } while (!action.equals("6"));
    }

    public void teacherMenu() {
        System.out.println("""
                [1]. Sukurti Egzamina
                [2]. Koreguoti Egzamina
                [3]. Ištrinti Egzamina
                [4]. Peržiureti studentu rezultatus
                [5]. Peržiureti egzaminu statistika
                [6]. Atsijungti
                """);
    }

    public void checkExams(Scanner scanner) {
        exams = examService.getExams();

        if (exams.isEmpty()) {
            System.out.println("Nera jokiu egzaminu");
            return;
        }
        exams.stream().map(Exam::getName).forEach(System.out::println);
        Exam exam = getExam(scanner);

        examStats = exam.getExamStats();
        if (examStats.isEmpty()) {
            System.out.println("Nera jokios statistikos");
            return;
        }
        for (ExamStats stats : examStats) {
            System.out.printf("%s - a: %d | b: %d | c: %d | teisingi: %d | neteisingi: %d\n", stats.getExam().getName(), stats.getAnsweredA(), stats.getAnsweredB(), stats.getAnsweredC(), stats.getAnsweredCorrectly(), stats.getAnsweredIncorrectly());
        }
    }

    public void checkAverage(Scanner scanner) {
        List<User> users = userService.getUsers();

        for (User user : users) {
            if (user.getType().equals(UserType.STUDENT.getRoleName())) {
                System.out.printf("%d. %s %s\n", user.getId(), user.getName(), user.getSurname());
            }
        }
        User user = getUser(users, scanner);

        Student student = new Student();
        student.checkAverage(user);
    }

    private User getUser(List<User> users, Scanner scanner) {
        while (true) {
            System.out.println("Iveskite studento numeri");
            int studentNum = getWholeNumber(scanner);
            User user = users.stream().filter(n -> n.getId() == studentNum).findFirst().orElse(null);
            if (user == null) {
                System.out.printf("Studentas su id %d nerastas\n", studentNum);
                continue;
            }
            System.out.printf("Ziurite %s %s vidurkius\n", user.getName(), user.getSurname());
            return user;
        }
    }

    private void deleteExam(Scanner scanner) {
        exams = examService.getExams();
        if (exams.isEmpty()) {
            System.out.println("Nera jokiu egzaminu");
            return;
        }
        exams.stream().map(Exam::getName).forEach(System.out::println);
        System.out.println("Irasykite pavadinima egzamino kuri norite istrinti");
        String examName = scanner.nextLine();
        Exam exam = exams.stream().filter(n -> n.getName().equals(examName)).findFirst().orElse(null);
        if (exam == null) {
            System.out.printf("Egzamino pavadinimu %s nera\n", examName);
            return;
        }
        System.out.printf("Egzaminas pavadinimu %s buvo istrintas\n", examName);
        exams.remove(exam);
        examService.deleteExam(exam);
    }

    private void createExam(Scanner scanner) {
        String examTitle = getUniqueExamName(scanner);
        if (examTitle == null) {
            return;
        }
        Exam exam = new Exam(examTitle);
        exams.add(exam);
        examService.createExam(exam);

        createQuestions(exam, scanner);
    }

    private void createQuestions(Exam exam, Scanner scanner) {

        System.out.println("Kiek bus klausymu?");
        int questionAmount = getWholeNumber(scanner);

        for (int i = 1; i < questionAmount + 1; i++) {
            System.out.printf("Iveskite %s klausyma\n", i);
            String question = scanner.nextLine();

            System.out.println("Iveskite pirma atsakyma");
            String answerA = scanner.nextLine();

            System.out.println("Iveskite antra atsakyma");
            String answerB = scanner.nextLine();

            System.out.println("Iveskite trecia atsakyma");
            String answerC = scanner.nextLine();

            System.out.printf("a. %s | b. %s | c. %s\n", answerA, answerB, answerC);
            System.out.printf("Iveskite %d-jo klausymo teisingo atsakymo raide\n", i);
            String correctAnswer = getCorrectAnswer(scanner);

            Questions newQuestion = new Questions(question, answerA, answerB, answerC, correctAnswer);

            questions.add(newQuestion);

            newQuestion.setExam(exam);

            examService.createQuestions(newQuestion);
        }
    }

    private void editExam(Scanner scanner) {
        exams = examService.getExams();
        if (exams.isEmpty()) {
            System.out.println("Nera jokiu egzaminu");
            return;
        }
        exams.stream().map(Exam::getName).forEach(System.out::println);
        Exam exam = getExam(scanner);
        String action;
        do {
            editExamMenu();
            action = scanner.nextLine();
            switch (action) {
                case "1" -> addToExam(scanner, exam);
                case "2" -> editExamQuestion(scanner, exam);
                case "3" -> System.out.println("Grizote i pagrindini menu");
                default -> System.out.println("Tokio veiksmo nera");
            }
        } while (!action.equals("3"));
    }

    private void editExamQuestion(Scanner scanner, Exam exam) {
        Questions examQuestion = getExamQuestion(scanner, exam);


        System.out.printf("Iveskite nauja %s-a klausyma\n", examQuestion.getId());
        String newQuestion = scanner.nextLine();

        System.out.println("Iveskite pirma atsakyma");
        String answerA = scanner.nextLine();

        System.out.println("Iveskite antra atsakyma");
        String answerB = scanner.nextLine();

        System.out.println("Iveskite trecia atsakyma");
        String answerC = scanner.nextLine();

        System.out.printf("a. %s | b. %s | c. %s\n", answerA, answerB, answerC);
        System.out.printf("Iveskite %d-jo klausymo naujo teisingo atsakymo raide\n", examQuestion.getId());
        String correctAnswer = getCorrectAnswer(scanner);

        examQuestion.setQuestion(newQuestion);
        examQuestion.setAnswerA(answerA);
        examQuestion.setAnswerB(answerB);
        examQuestion.setAnswerC(answerC);
        examQuestion.setCorrectAnswer(correctAnswer);


        examService.updateQuestion(examQuestion);

        System.out.printf("Pakeitete %s egzamino %s-a klausyma\n", exam.getName(), examQuestion.getId());
    }

    private Questions getExamQuestion(Scanner scanner, Exam exam) {

        questions = exam.getQuestions();

        for (Questions question : questions) {
            System.out.printf("%d. %s | a. %s | b. %s | c. %s | Teisingas atsakymas? %s\n", question.getId(), question.getQuestion(), question.getAnswerA(), question.getAnswerB(), question.getAnswerC(), question.getCorrectAnswer());
        }

        System.out.printf("Iveskite kuri egzamino %s klausima norite pakeisti\n", exam.getName());
        while (true) {
            try {
                Long questionNumb = Long.parseLong(scanner.nextLine());

                Questions question = questions.stream()
                        .filter(q -> q.getId().equals(questionNumb))
                        .findFirst()
                        .orElse(null);

                if (question == null) {
                    System.out.println("Tokio klausymo nera");
                    continue;
                }
                return question;
            } catch (Exception e) {
                System.out.println("Ivedete bloga skaiciu");
            }

        }
    }

    private void addToExam(Scanner scanner, Exam exam) {

        System.out.println("Iveskite papildoma klausyma: ");
        String question = scanner.nextLine();

        System.out.println("Iveskite pirma atsakyma: ");
        String answerA = scanner.nextLine();

        System.out.println("Iveskite antra atsakyma: ");
        String answerB = scanner.nextLine();

        System.out.println("Iveskite trecia atsakyma: ");
        String answerC = scanner.nextLine();

        System.out.printf("a. %s | b. %s | c. %s\n", answerA, answerB, answerC);
        System.out.println("Iveskite papildomo klausymo teisingo atsakymo raide");
        String correctAnswer = getCorrectAnswer(scanner);

        Questions newQuestion = new Questions(question, answerA, answerB, answerC, correctAnswer);

        newQuestion.setExam(exam);
        questions.add(newQuestion);

        examService.createQuestions(newQuestion);
        System.out.printf("Pridejote papildoma klausyma prie %s egzamino\n", exam.getName());

    }

    private Exam getExam(Scanner scanner) {
        while (true) {
            System.out.println("Iveskite egzamino pavadinima");
            String examName = scanner.nextLine();
            Exam exam = exams.stream().filter(n -> n.getName().equals(examName)).findFirst().orElse(null);
            if (exam == null) {
                System.out.printf("Egzamino pavadinimu %s nera\n", examName);
                continue;
            }
            System.out.printf("Atidarete egzamina %s\n", examName);
            return exam;
        }
    }

    private void editExamMenu() {
        System.out.println("""
                [1]. Prideti papildoma klausyma
                [2]. Pakeisti klausyma
                [3]. Grizti atgal""");
    }

    private String getUniqueExamName(Scanner scanner) {
        System.out.println("Irasykite egzamino pavadinima");
        String newExamName = scanner.nextLine();

        exams = examService.getExams();
        Exam existingExam = exams.stream()
                .filter(n -> n.getName().equals(newExamName))
                .findFirst()
                .orElse(null);

        if (existingExam != null) {
            System.out.printf("Egzaminas %s jau existuoja\n", newExamName);
            return null;
        }
        return newExamName;
    }

    public int getWholeNumber(Scanner scanner) {
        while (true) {
            try {
                int number = Integer.parseInt(scanner.nextLine());

                if (number <= 0) {
                    System.out.println("Atsakymas negali buti mažesnis už 0");
                    continue;
                }
                return number;
            } catch (NumberFormatException e) {
                System.out.println("Blogai ivestas skaičius, bandykite vėl");
            }
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
}
