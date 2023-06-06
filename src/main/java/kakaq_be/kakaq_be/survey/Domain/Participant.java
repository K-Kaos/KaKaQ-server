package kakaq_be.kakaq_be.survey.Domain;

import jakarta.persistence.*;
import kakaq_be.kakaq_be.user.Domain.User;
import lombok.*;

@Data
@Builder
@Entity
@Table( name = "participant" )
@IdClass(ParticipantID.class)
public class Participant {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    public Participant() {
    }

    public Participant(User user, Survey survey) {
        this.user = user;
        this.survey = survey;
    }
}
