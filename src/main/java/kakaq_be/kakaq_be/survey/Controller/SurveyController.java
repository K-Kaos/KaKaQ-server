package kakaq_be.kakaq_be.survey.Controller;
import org.springframework.web.client.RestTemplate;
import kakaq_be.kakaq_be.survey.Domain.Question;
import kakaq_be.kakaq_be.survey.Domain.Response;
import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.survey.Dto.QuestionDto;
import kakaq_be.kakaq_be.survey.Dto.ResponseDto;
import kakaq_be.kakaq_be.survey.Dto.SurveyDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import kakaq_be.kakaq_be.survey.Service.QuestionService;
import kakaq_be.kakaq_be.survey.Service.ResponseService;
import kakaq_be.kakaq_be.survey.Service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;


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
    public Survey createSurvey(@Valid @RequestBody Survey survey) {
        return surveyService.createSurvey(survey);
    }

    // Get all surveys
    @GetMapping("/surveys")
    public List<Survey> getAllSurveys() {
        return surveyService.getAllSurveys();
    }

    // Get a survey by id
    @GetMapping("/surveys/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable(value = "id") Long surveyId)
            throws ResourceNotFoundException {
        Survey survey = surveyService.getSurveyById(surveyId);
        return ResponseEntity.ok().body(survey);
    }

    // Get surveys by user id
//    @GetMapping("/surveys/user/{userId}")
//    public List<Survey> getSurveysByUserId(@PathVariable(value = "userId") Long userId)
//            throws ResourceNotFoundException {
//        return surveyService.getSurveysByUserId(userId);
//    }

    // Update a survey
    @PutMapping("/survey/{id}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable(value = "id") Long surveyId,
                                               @Valid @RequestBody Survey survey) throws ResourceNotFoundException {
        Survey updatedSurvey = surveyService.updateSurvey(surveyId, survey);
        return ResponseEntity.ok(updatedSurvey);
    }

    // Delete a survey
    @DeleteMapping("/survey/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSurvey(@PathVariable(value = "id") Long surveyId)
            throws ResourceNotFoundException {
        surveyService.deleteSurvey(surveyId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    // Get all questions for a survey
    @GetMapping("/survey/{id}/questions")
    public List<Question> getAllQuestionsForSurvey(@PathVariable(value = "id") Long surveyId)
            throws ResourceNotFoundException {
        return questionService.getAllQuestionsForSurvey(surveyId);
    }

    // Get a question by id
    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable(value = "id") Long questionId)
            throws ResourceNotFoundException {
        Question question = questionService.getQuestionById(questionId);
        return ResponseEntity.ok().body(question);
    }

    // Create a new response
    @PostMapping("/response/create")
    public Response createResponse(@Valid @RequestBody Response response) {
        return responseService.createResponse(response);
    }

    // Get all responses for a survey
    @GetMapping("/surveys/{id}/responses")
    public List<Response> getAllResponsesForSurvey(@PathVariable(value = "id") Long surveyId)
            throws ResourceNotFoundException {
        return responseService.getAllResponsesForSurvey(surveyId);
    }

    String gpt_API_KEY = "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";
    @GetMapping("/survey/chatbot")
    public String sendTopic(HttpServletRequest param) {
        System.out.println(param);
        String topic = param.getParameter("topic");
        //api에 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + gpt_API_KEY);
        //이 아래 url로 모델 바꿀수 있음.
        String url = "https://api.openai.com/v1/engines/text-davinci-003/completions";

        // 보낼 본문 데이터 설정
        System.out.println(topic);
        String prompt = topic + "에 대한 설문조사 질문지 폼을 객관식 질문으로 5개 만들어주는데, 선택지는 1, 2, 3, 4와 마지막 5.기타로 해줘";
        System.out.println(prompt);
        String requestBody = "{\"prompt\":\"" + prompt + "\",\"max_tokens\":3900,\"temperature\":0.7}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // API에 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
//        ChatMessage responseChat = new ChatMessage();
        System.out.println(response.getBody());

        // API 응답 결과 이거 format 만들어서 맞게 만들어야 함.
        return response.getBody();
    }

}