package kakaq_be.kakaq_be.survey.Domain;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantID implements Serializable {
    private Long user;
    private Long survey;
}