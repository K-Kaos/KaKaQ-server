package kakaq_be.kakaq_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSignupRequestDto {
    private String email;
    private String password;
    private String name;
}
