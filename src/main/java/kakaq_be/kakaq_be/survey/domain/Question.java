package kakaq_be.kakaq_be.survey.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "question" )
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String text;


    @ManyToOne
    private QuestionType type;

    @ElementCollection
    private List<String> options;

    @ManyToOne
    private Survey survey;

    @OneToMany(mappedBy = "question")
    private List<Response> responses;

}
