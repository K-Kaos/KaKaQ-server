package kakaq_be.kakaq_be.survey.Service;

import kakaq_be.kakaq_be.survey.Domain.Question;
import kakaq_be.kakaq_be.survey.Repository.QuestionRepository;
import kakaq_be.kakaq_be.user.Domain.User;
import kakaq_be.kakaq_be.user.Repository.UserRepository;
import kakaq_be.kakaq_be.survey.Repository.SurveyRepository;
import kakaq_be.kakaq_be.survey.Domain.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private QuestionRepository questionRepository;




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

//    public List<Survey> getSurveysByUserId(Long userId) {
//        User user = userRepository.findById(userId.toString())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
//        return surveyRepository.findByUserId(userId);
//    }

    //Remove all questions from survey
    public void removeQuestionFromSurvey(Long surveyId) {
        Optional<Survey> surveyOptional = surveyRepository.findSurveyById(surveyId);
        if (surveyOptional.isPresent()) {
            Survey survey = surveyOptional.get();
            List<Question> questions = survey.getQuestions();

            // 질문 삭제
            for (Question question : questions) {
                questionRepository.delete(question);
            }
            questions.clear(); // 질문 목록 비우기
            survey.setQuestions(questions);
            surveyRepository.save(survey);
        } else {
            throw new ResourceNotFoundException("해당 id를 가진 survey를 찾을 수 없습니다.");
        }
    }

    // Update a survey
    public Survey updateSurvey(Long surveyId, Survey surveyDetails) {
        Optional<Survey> surveyEntityWrapper = surveyRepository.findSurveyById(surveyId);
        Survey surveyEntity = surveyEntityWrapper.orElseThrow(
                ()->new UsernameNotFoundException("해당 id을 가진 survey를 찾을 수 없습니다."));
        surveyEntity.setTitle(surveyDetails.getTitle());
        surveyEntity.setKeyword(surveyDetails.getKeyword());
        surveyEntity.setCity(surveyDetails.getCity());
        surveyEntity.setStartDate(surveyDetails.getStartDate());
        surveyEntity.setEndDate(surveyDetails.getEndDate());
        surveyEntity.setPublicState(surveyDetails.getPublicState());
        return surveyRepository.save(surveyEntity);
    }

    // Delete a survey
    public void deleteSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResourceNotFoundException("Survey not found for this id :: " + surveyId));
        surveyRepository.delete(survey);
    }

    // Search keyword
    public List<Survey> searchSurveys(String keyword) {
        return surveyRepository.findByKeywordsContaining(keyword);
    }

}
