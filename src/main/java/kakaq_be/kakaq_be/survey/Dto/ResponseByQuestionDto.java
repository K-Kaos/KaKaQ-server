package kakaq_be.kakaq_be.survey.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseByQuestionDto {
    private String value;
    private String answerer;
    private String answererRole;
    private Long count;
}
