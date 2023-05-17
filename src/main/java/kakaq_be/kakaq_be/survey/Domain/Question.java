package kakaq_be.kakaq_be.survey.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "question" )
@ToString(exclude = {"type", "survey", "responses"})
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long question_id;

    @Column(nullable = false, length = 50)
    private String text;

    @ManyToOne
    @JoinColumn(name = "question_type_id", nullable = false)
    private QuestionType type;

    @ElementCollection
    private List<String> options;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "question")
    private List<Response> responses;

    @Builder
    public Question(Long question_id, String text, QuestionType type,List<String> options, Survey survey){
        this.question_id = question_id;
        this.text = text;
        this.type = type;
        this.options = options;
        this.survey = survey;
    }


}