package kakaq_be.kakaq_be.survey.Repository;

import kakaq_be.kakaq_be.survey.Domain.Participant;
import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.user.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsBySurveyAndUser(Survey survey, User user);

    List<Participant> findByUserId(Long userId);

    @Query("SELECT sp.user From Participant sp WHERE sp.survey.id = :surveyId")
    List<User> findUsersBySurveyId(Long surveyId);
}
