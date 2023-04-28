package kakaq_be.kakaq_be.survey.Service;

import kakaq_be.kakaq_be.user.Domain.User;
import kakaq_be.kakaq_be.user.Repository.UserRepository;
import kakaq_be.kakaq_be.survey.Repository.SurveyRepository;
import kakaq_be.kakaq_be.survey.Domain.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new survey
    public Survey createSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }

    // Get all surveys
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    // Get a survey by id
    public Survey getSurveyById(Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found for this id :: " + surveyId));
    }

    public List<Survey> getSurveysByUserId(Long userId) {
        User user = userRepository.findById(userId.toString())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return surveyRepository.findByUserId(userId);
    }


    // Update a survey
    public Survey updateSurvey(Long surveyId, Survey surveyDetails) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResourceNotFoundException("Survey not found for this id :: " + surveyId));
        survey.setTitle(surveyDetails.getTitle());
        survey.setKeyword(surveyDetails.getKeyword());
        survey.setQuestions(surveyDetails.getQuestions());
        return surveyRepository.save(survey);
    }

    // Delete a survey
    public void deleteSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResourceNotFoundException("Survey not found for this id :: " + surveyId));
        surveyRepository.delete(survey);
    }
}
