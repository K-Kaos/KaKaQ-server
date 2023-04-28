package kakaq_be.kakaq_be.survey.Service;

import kakaq_be.kakaq_be.survey.Repository.ResponseRepository;
import kakaq_be.kakaq_be.survey.Domain.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResponseService {
    @Autowired
    private ResponseRepository responseRepository;

    // Create a new response
    public Response createResponse(Response response) {
        return responseRepository.save(response);
    }

    // Get all responses for a survey
    public List<Response> getAllResponsesForSurvey(Long surveyId) {
        return responseRepository.findBySurveyId(surveyId);
    }
}
