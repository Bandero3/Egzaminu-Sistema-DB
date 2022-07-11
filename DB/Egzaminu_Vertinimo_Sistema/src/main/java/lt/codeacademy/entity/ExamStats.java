package lt.codeacademy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "examStats")
public class ExamStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int answeredA;
    private int answeredB;
    private int answeredC;
    private int answeredCorrectly;
    private int answeredIncorrectly;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    public ExamStats(int answeredA, int answeredB, int answeredC, int answeredCorrectly, int answeredIncorrectly) {
        this.answeredA = answeredA;
        this.answeredB = answeredB;
        this.answeredC = answeredC;
        this.answeredCorrectly = answeredCorrectly;
        this.answeredIncorrectly = answeredIncorrectly;
    }
}
