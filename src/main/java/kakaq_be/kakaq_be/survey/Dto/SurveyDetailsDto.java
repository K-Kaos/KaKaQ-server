package kakaq_be.kakaq_be.survey.Dto;

import kakaq_be.kakaq_be.user.Domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyDetailsDto {
    private Long id;
    private String title;
    private String publicState;
    private Date startDate;
    private Date endDate;
    private String creator;
    private List<String> keywords;
    private String city;
    private String status;
    private List<QuestionDetailsDto> questions;
}
