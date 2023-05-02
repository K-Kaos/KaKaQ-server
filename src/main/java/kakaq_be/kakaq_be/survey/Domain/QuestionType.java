package kakaq_be.kakaq_be.survey.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "QuestionType" )
public class QuestionType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long question_type_id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "type")
    private List<Question> questions;

    @Builder
    public QuestionType(long question_type_id, String name, List<Question> questions){
        this.question_type_id = question_type_id;
        this.name = name;
        this.questions=questions;
    }
}
