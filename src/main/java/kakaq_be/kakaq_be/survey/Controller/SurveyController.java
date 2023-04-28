package kakaq_be.kakaq_be.survey.Controller;

import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.survey.Dto.QuestionDto;
import kakaq_be.kakaq_be.survey.Dto.ResponseDto;
import kakaq_be.kakaq_be.survey.Dto.SurveyDto;
import kakaq_be.kakaq_be.survey.Service.QuestionService;
import kakaq_be.kakaq_be.survey.Service.ResponseService;
import kakaq_be.kakaq_be.survey.Service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ResponseService responseService;

    // Create a new survey
    @PostMapping("/survey/create")
    public Survey createSurvey(@Valid @RequestBody SurveyDto surveyDto) {
        return surveyService.createSurvey(surveyDto);
    }

    // Get all surveys
    @GetMapping("/surveys")
    public List<Survey> getAllSurveys() {
        return surveyService.getAllSurveys();
    }

    // Get a survey by id
    @GetMapping("/surveys/{id}")
    public ResponseEntity<SurveyDto> getSurveyById(@PathVariable(value = "id") Long surveyId)
            throws ResourceNotFoundException {
        SurveyDto surveyDto = surveyService.getSurveyById(surveyId);
        return ResponseEntity.ok().body(surveyDto);
    }

    // Get surveys by user id
    @GetMapping("/surveys/user/{userId}")
    public List<Survey> getSurveysByUserId(@PathVariable(value = "userId") Long userId)
            throws ResourceNotFoundException {
        return surveyService.getSurveysByUserId(userId);
    }

    // Update a survey
    @PutMapping("/survey/{id}")
    public ResponseEntity<SurveyDto> updateSurvey(@PathVariable(value = "id") Long surveyId,
                                                  @Valid @RequestBody SurveyDto surveyDto) throws ResourceNotFoundException {
        SurveyDto updatedSurveyDto = surveyService.updateSurvey(surveyId, surveyDto);
        return ResponseEntity.ok(updatedSurveyDto);
    }

    // Delete a survey
    @DeleteMapping("/survey/{id}")
    public Map<String, Boolean> deleteSurvey(@PathVariable(value = "id") Long surveyId)
            throws ResourceNotFoundException {
        surveyService.deleteSurvey(surveyId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    // Get all questions for a survey
    @GetMapping("/survey/{id}/questions")
    public List<QuestionDto> getAllQuestionsForSurvey(@PathVariable(value = "id") Long surveyId)
            throws ResourceNotFoundException {
        return questionService.getAllQuestionsForSurvey(surveyId);
    }

    // Get a question by id
    @GetMapping("/question/{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable(value = "id") Long questionId)
            throws ResourceNotFoundException {
        QuestionDto questionDto = questionService.getQuestionById(questionId);
        return ResponseEntity.ok().body(questionDto);
    }

    // Create a new response
    @PostMapping("/response/create")
    public ResponseDto createResponse(@Valid @RequestBody ResponseDto responseDto) {
        return responseService.createResponse(responseDto);
    }

    // Get all responses for a survey
    @GetMapping("/surveys/{id}/responses")
    public List<ResponseDto> getAllResponsesForSurvey(@PathVariable(value = "id") Long surveyId)
            throws ResourceNotFoundException {
        return responseService.getAllResponsesForSurvey(surveyId);
    }
}
