package kakaq_be.kakaq_be.survey.Service;

import kakaq_be.kakaq_be.survey.Domain.QuestionType;
import kakaq_be.kakaq_be.survey.Repository.QuestionRepository;
import kakaq_be.kakaq_be.survey.Domain.Question;
import kakaq_be.kakaq_be.survey.Repository.QuestionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionTypeRepository questionTypeRepository;
    // Get all questions for a survey
    public List<Question> getAllQuestionsForSurvey(Long surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }

    // Get a question by id
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question not found for this id :: " + questionId));
    }

    // Get questionType by name
    public QuestionType getQuestiontypeByName(String name){
        return questionTypeRepository.findQuestionTypeByName(name).orElseThrow(() -> new ResourceNotFoundException("Questiontype not found for this name::"+name));

    }

}
