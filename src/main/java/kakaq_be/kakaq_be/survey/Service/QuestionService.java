package kakaq_be.kakaq_be.survey.Service;

import kakaq_be.kakaq_be.survey.Repository.QuestionRepository;
import kakaq_be.kakaq_be.survey.Domain.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    // Get all questions for a survey
    public List<Question> getAllQuestionsForSurvey(Long surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }

    // Get a question by id
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question not found for this id :: " + questionId));
    }
}
