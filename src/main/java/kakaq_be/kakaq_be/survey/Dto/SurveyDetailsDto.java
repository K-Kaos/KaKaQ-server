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
public class SurveyDetailsDto {
    private Long id;
    private String title;
    private String publicState;
    private List<String> keyword;
    private String city;
    private String status;
    private List<QuestionDetailsDto> questions;
}
