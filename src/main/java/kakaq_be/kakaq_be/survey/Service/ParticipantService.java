package kakaq_be.kakaq_be.survey.Service;

import kakaq_be.kakaq_be.survey.Repository.ParticipantRepository;
import kakaq_be.kakaq_be.user.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ParticipantService {

    @Autowired
    private ParticipantRepository surveyParticipantRepository;

    public List<User> getUsersBySurveyId(Long surveyId) {
        return surveyParticipantRepository.findUsersBySurveyId(surveyId);
    }
}
