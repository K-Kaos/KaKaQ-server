package kakaq_be.kakaq_be.survey.Dto;

import kakaq_be.kakaq_be.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyDto {
    private Long id;
    private String name;
    private List<String> keyword;
    private User creator;
    private List<QuestionDto> questions;
    private List<ResponseDto> responses;
}

