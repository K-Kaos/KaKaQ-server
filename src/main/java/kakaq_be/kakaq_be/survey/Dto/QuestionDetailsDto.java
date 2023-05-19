package kakaq_be.kakaq_be.survey.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDetailsDto {
    private Long question_id;
    private String text;
    private QuestionTypeDetailsDto type;
    private List<String> options;
}
