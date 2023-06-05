package kakaq_be.kakaq_be.user.Dto;

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
public class UserDto {
    private Long id;
    private String username;
    //private String password;
    private String email;
    private String role;
}
