package kakaq_be.kakaq_be.survey.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionTypeDetailsDto {
    private Long question_type_id;
    private String name;
}
