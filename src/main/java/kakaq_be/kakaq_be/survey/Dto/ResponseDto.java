package kakaq_be.kakaq_be.survey.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {
    private Long id;
    private Long questionId;
    private String value;
//    private Map<String,Long> optionCounts;
//    private QuestionDetailsDto question;
}
