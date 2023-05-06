package kakaq_be.kakaq_be.survey.Controller;

import kakaq_be.kakaq_be.survey.Domain.QuestionType;
import kakaq_be.kakaq_be.survey.Repository.*;
import kakaq_be.kakaq_be.user.Domain.User;
import kakaq_be.kakaq_be.user.Repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import kakaq_be.kakaq_be.survey.Repository.SurveyRepository;
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


import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SurveyController {
    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    QuestionTypeRepository questionTypeRepository;

    @Autowired
    ResponseRepository responseRepository;

    // Create a new survey
    @PostMapping("/survey/create")//Create new survey
    public String createSurvey(@RequestBody Survey survey) {
        System.out.println(survey);
        Optional<User> userEntityWrapper = userRepository.findByEmail(survey.getCreator().getEmail());
        User userEntity = userEntityWrapper.orElseThrow(
                ()->new UsernameNotFoundException("해당 id을 가진 사용자를 찾을 수 없습니다."));
        Survey new_survey = new Survey(survey.getId(), survey.getTitle(), survey.getCity(), survey.getStartDate(), survey.getEndDate(), survey.getPublicState(), userEntity);
        System.out.println(new_survey);
        surveyRepository.save(new_survey);
        return Long.toString(new_survey.getId());
    }

    @PostMapping("/survey/question")//Create new survey's questions
    public Long createQuestion(@RequestBody Question question, @RequestParam("surveyId") Long surveyId){
        System.out.println(question);
        Optional<Survey> surveyEntityWrapper = surveyRepository.findSurveyById(surveyId);
        Survey surveyEntity = surveyEntityWrapper.orElseThrow(
                ()->new UsernameNotFoundException("해당 id을 가진 survey를 찾을 수 없습니다."));
        Optional<QuestionType> typeEntityWrapper = questionTypeRepository.findQuestionTypeByName(question.getType().getName());
        QuestionType typeEntity = typeEntityWrapper.orElseThrow(() -> new UsernameNotFoundException("해당 name을 가진 type을 찾을 수 없습니다."));
        Question new_question = new Question(question.getQuestion_id(),question.getText(),typeEntity, question.getOptions(), surveyEntity);
        System.out.println(new_question);
        questionRepository.save(new_question);
        return new_question.getQuestion_id();
    }


    @PostMapping("/survey/editquestions")//fill the survey's questions column with new questions
    public String editSurveyQuestions(@RequestBody Question data, @RequestParam("surveyId") Long surveyId) {
        Long question_index = data.getQuestion_id();
        String answer = "fail";
        Optional<Survey> surveyOptional = surveyRepository.findById(surveyId);
        if (surveyOptional.isPresent()) {
            Survey survey = surveyOptional.get();
            Optional<Question> questionOptional = questionRepository.findById(question_index);
            if (questionOptional.isPresent()) {
                Question question = questionOptional.get();
                survey.addQuestion(question);
                answer = "success";
            }
            surveyRepository.save(survey);
        }
        return answer;
    }


    //participate the survey, fill the response table
    @PostMapping("/survey/participate")
    public String surveyParticipate(@RequestBody Response response, @RequestParam("surveyId") Long surveyId, @RequestParam("questionId") Long questionId){
        System.out.println(response);
        Optional<Survey> surveyEntityWrapper = surveyRepository.findSurveyById(surveyId);
        Survey surveyEntity = surveyEntityWrapper.orElseThrow(()->new UsernameNotFoundException("해당 id을 가진 survey를 찾을 수 없습니다."));
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        Question questionEntity = questionOptional.orElseThrow(()->new UsernameNotFoundException("해당 id을 가진 question를 찾을 수 없습니다."));
        Optional<User> userEntityWrapper = userRepository.findByEmail(response.getUser().getEmail());
        User userEntity = userEntityWrapper.orElseThrow(()->new UsernameNotFoundException("해당 id을 가진 사용자를 찾을 수 없습니다."));

        Response new_response = new Response(response.getResponse_id(), response.getText(), questionEntity, surveyEntity, userEntity);
        responseRepository.save(new_response);
        return Long.toString(new_response.getResponse_id());
    }

    // Get public surveys
    @GetMapping("/surveys")
    public List<Survey> getPublicSurveys() {
        List<Survey> surveyEntityWrapper = surveyRepository.findAllByPublicState("public");
        return surveyEntityWrapper;
    }

    //survey URL share
    @GetMapping("/surveys/{id}")
    public ResponseEntity<String> getSurveyUrl(@PathVariable("id") Long surveyId) {
        Optional<Survey> surveyOptional = surveyRepository.findById(surveyId);
        System.out.println("Reached the endpoint");
        if (surveyOptional.isEmpty()) {
            String errorMsg = "This survey is currently unavailable!";
            System.out.println(errorMsg);
            return ResponseEntity.notFound().build();
        }
        Survey survey = surveyOptional.get();
        String surveyURL = String.format("https://localhost:8080/api/surveys/%d", survey.getId());
        return ResponseEntity.ok(surveyURL);
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
    @GetMapping("/survey/chatbot")//Create questions using chat-gpt api
    public String sendTopic(HttpServletRequest param) {
        String topic = param.getParameter("topic");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + gpt_API_KEY);
        //이 아래 url로 모델 바꿀수 있음.
        String url = "https://api.openai.com/v1/engines/text-davinci-003/completions";

        // 보낼 본문 데이터 설정
        System.out.println(topic);
        String prompt = topic + "에 대한 설문조사 질문지 폼을 객관식 질문으로 5개 만들어주는데, 선택지는 1, 2, 3, 4와 마지막 5.기타로 해줘 그리고 질문과 선택지마다 줄바꿈 한번씩만 해줘";
        System.out.println(prompt);
        String requestBody = "{\"prompt\":\"" + prompt + "\",\"max_tokens\":3900,\"temperature\":0.7}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // API에 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println(response.getBody());

        return response.getBody();
    }

}
