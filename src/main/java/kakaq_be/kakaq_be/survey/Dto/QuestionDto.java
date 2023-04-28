package kakaq_be.kakaq_be.survey.Dto;

import kakaq_be.kakaq_be.survey.Domain.Response;
import kakaq_be.kakaq_be.survey.Domain.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private Long id;
    private String text;
    private String type;
    private List<String> options;
    private Survey survey;
    private List<Response> responses;
}

