package kakaq_be.kakaq_be.survey.Controller;

import kakaq_be.kakaq_be.survey.Domain.*;
import kakaq_be.kakaq_be.survey.Dto.*;
import kakaq_be.kakaq_be.survey.Repository.*;
import kakaq_be.kakaq_be.user.Domain.User;
import kakaq_be.kakaq_be.user.Dto.UserDto;
import kakaq_be.kakaq_be.user.Repository.UserRepository;
import kakaq_be.kakaq_be.user.Service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import kakaq_be.kakaq_be.survey.Repository.SurveyRepository;
import org.springframework.web.client.RestTemplate;
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
import java.util.*;


@RestController
@RequestMapping("/api")
public class SurveyController {


    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    QuestionTypeRepository questionTypeRepository;

    @Autowired
    ResponseRepository responseRepository;

    //get survey+question
    @GetMapping("/surveys/get/{id}")
    public SurveyDetailsDto getSurvey(@PathVariable Long id){
        Optional<Survey> surveyEntityWrapper = surveyRepository.findSurveyById(id);
        Survey survey = surveyEntityWrapper.orElseThrow(
                ()->new UsernameNotFoundException("해당 id을 가진 survey를 찾을 수 없습니다."));

        SurveyDetailsDto surveyDTO = new SurveyDetailsDto();
        surveyDTO.setId(survey.getId());
        surveyDTO.setTitle(survey.getTitle());
        surveyDTO.setPublicState(survey.getPublicState());
        surveyDTO.setKeywords(survey.getKeywords());
        surveyDTO.setCity(survey.getCity());
        surveyDTO.setStartDate(survey.getStartDate());
        surveyDTO.setEndDate(survey.getEndDate());
        surveyDTO.setCategory(survey.getCategory());

        List<QuestionDetailsDto> questionDTOs = new ArrayList<>();
        for (Question question : survey.getQuestions()) {
            QuestionDetailsDto questionDTO = new QuestionDetailsDto();
            questionDTO.setQuestion_id(question.getQuestion_id());
            questionDTO.setText(question.getText());

            QuestionType questionType = question.getType();
            QuestionTypeDetailsDto questionTypeDTO = new QuestionTypeDetailsDto();
            questionTypeDTO.setQuestion_type_id(questionType.getQuestion_type_id());
            questionTypeDTO.setName(questionType.getName());

            questionDTO.setType(questionTypeDTO);
            questionDTO.setOptions(question.getOptions());

            questionDTOs.add(questionDTO);
        }

        surveyDTO.setQuestions(questionDTOs);

        return surveyDTO;
    }

    // Create a new survey
    @PostMapping("/survey/create")
    public String createSurvey(@RequestBody Survey survey) {
        System.out.println(survey);

        Optional<User> userEntityWrapper = userRepository.findByEmail(survey.getCreator().getEmail());
        User userEntity = userEntityWrapper.orElseThrow(
                ()->new UsernameNotFoundException("해당 id을 가진 사용자를 찾을 수 없습니다."));

        // FIX: Use getKeywords() method instead of getKeyword()
        Survey new_survey = new Survey(survey.getId(), survey.getTitle(), survey.getCategory(), survey.getCity(), survey.getStartDate(), survey.getEndDate(), survey.getPublicState(), userEntity, survey.getKeywords());

        System.out.println(new_survey);
        surveyRepository.save(new_survey);
        return Long.toString(new_survey.getId());
    }

    @PostMapping("/survey/question")//Create new survey's questions
    public String createQuestion(@RequestBody Question question, @RequestParam("surveyId") Long surveyId){

        //fill new question to question table
        Survey surveyEntity = surveyService.getSurveyById(surveyId);
        QuestionType typeEntity = questionService.getQuestiontypeByName(question.getType().getName());
        Question new_question = new Question(question.getQuestion_id(),question.getText(),typeEntity, question.getOptions(), surveyEntity);
        questionRepository.save(new_question);

        //update survey's questions column with new question
        String answer = "fail";
        Optional<Survey> surveyOptional = surveyRepository.findById(surveyId);
        if (surveyOptional.isPresent()) {
            Survey survey = surveyOptional.get();
            Optional<Question> questionOptional = questionRepository.findById(new_question.getQuestion_id());
            if (questionOptional.isPresent()) {
                Question questionq = questionOptional.get();
                survey.addQuestion(questionq);
                answer = "success";
            }
            surveyRepository.save(survey);
        }
        return answer;
    }



    //participate the survey, fill the response table
    @PostMapping("/survey/participate")
    public String surveyParticipate(@RequestBody Response response, @RequestParam("surveyId") Long surveyId, @RequestParam("questionId") Long questionId){
        Survey surveyEntity = surveyService.getSurveyById(surveyId);
        Question questionEntity = questionService.getQuestionById(questionId);
        User userEntity = userService.findUserfromSurvey(response.getUser().getEmail());
        Response new_response = new Response(response.getResponse_id(), response.getText(), questionEntity, surveyEntity, userEntity);

        responseRepository.save(new_response);
        return Long.toString(new_response.getResponse_id());
    }

    // 응답 시, Participant(참여자) 테이블에 Survey와 User의 아이디 저장
    @GetMapping("/survey/participant")
    public String AddSurveyParticipant(@RequestParam("surveyId") Long surveyId, @RequestParam("userEmail") String userEmail){
        try{
            Survey surveyEntity = surveyService.getSurveyById(surveyId);
            User userEntity = userService.findUserfromSurvey(userEmail);
            // 중복 체크
            if (participantRepository.existsBySurveyAndUser(surveyEntity, userEntity)) {
                return "duplicate";
            }
            Participant participant = new Participant();
            participant.setSurvey(surveyEntity);
            participant.setUser(userEntity);
            participantRepository.save(participant);
            return "ok";
        }catch (Exception e) {
            return "fail";
        }
    }

    // Get public surveys
    @GetMapping("/surveys")
    public List<SurveyDetailsDto> getPublicSurveys() {
        List<Survey> surveyEntityWrapper = surveyRepository.findAllByPublicState("public");
        List<SurveyDetailsDto> surveyDTOs = new ArrayList<>();

        for(Survey survey : surveyEntityWrapper){
            SurveyDetailsDto surveyDTO = new SurveyDetailsDto();
            surveyDTO.setId(survey.getId());
            surveyDTO.setTitle(survey.getTitle());
            surveyDTO.setStartDate(survey.getStartDate());
            surveyDTO.setEndDate(survey.getEndDate());
            surveyDTO.setKeywords(survey.getKeywords());
            User user = survey.getCreator();
            surveyDTO.setCreator(user.getUsername());
            surveyDTO.setCategory(survey.getCategory());

            surveyDTOs.add(surveyDTO);
        }
        return surveyDTOs;
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
        String surveyURL = String.format("http://localhost:3000/participate/%d", survey.getId());
        return ResponseEntity.ok(surveyURL);
    }


    //get surveyID by title, keyword, category
    @GetMapping("/surveys/getSurveyId")
    public ResponseEntity<Long> createUrl(
            @RequestParam String title,
            @RequestParam String keywords,
            @RequestParam String category) {
        System.out.println("createUrl() called");
        System.out.println("Title: " + title);
        System.out.println("Keywords: " + keywords);
        System.out.println("Category: " + category);

        Long surveyId = surveyService.getSurveyIdByTitleKeywordsCategory(title, keywords, category);

        if (surveyId != null) {
            return ResponseEntity.ok(surveyId);
        } else {
            return ResponseEntity.notFound().build();
        }
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
        //설문의 질문삭제 - 설문 업데이트 - 퀘스쳔 생성, 연결
        surveyService.removeQuestionFromSurvey(surveyId);
        Survey updatedSurvey = surveyService.updateSurvey(surveyId, survey);
        //퀘스천 생성 아직 안함!!!!!!!
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

    // Filter by category and Sort by end_date
//    @GetMapping("/surveys/filter")
//    public List<Survey> sortSurveys(@RequestParam(required = false) String category) {
//        System.out.println("sortSurveys() called");
//        List<Survey> publicSurvey = surveyRepository.findAllByPublicState("public");
//        if (category != null) {
//            return surveyService.sortSurveys(publicSurvey, category);
//        }
//        System.out.println("There is no survey for this category!");
//        return publicSurvey;
//    }
    @GetMapping("/surveys/filter")
    public List<SurveyDetailsDto> sortSurveys(@RequestParam(required = false) String category) {
        System.out.println("sortSurveys() called");
        System.out.println("category is: " + category);
        List<Survey> publicSurveys = surveyRepository.findAllByPublicState("public");
        List<SurveyDetailsDto> surveyDetailsDtos = new ArrayList<>();

        // Check if category is provided and handle accordingly
        if (category != null) {
            publicSurveys.stream()
                    .filter(survey -> survey.getCategory().equalsIgnoreCase(category))
                    .sorted(Comparator.comparing(Survey::getEndDate))
                    .forEach(survey -> {
                        SurveyDetailsDto surveyDetailsDto = mapToSurveyDto(survey);
                        surveyDetailsDtos.add(surveyDetailsDto);
                    });
        } else {
            // No category provided, sort all public surveys
            publicSurveys.stream()
                    .sorted(Comparator.comparing(Survey::getEndDate))
                    .forEach(survey -> {
                        SurveyDetailsDto surveyDetailsDto = mapToSurveyDto(survey);
                        surveyDetailsDtos.add(surveyDetailsDto);
                    });
        }

        System.out.println("Filtered surveys: " + surveyDetailsDtos);
        return surveyDetailsDtos;
    }

    private SurveyDetailsDto mapToSurveyDto(Survey survey) {
        return SurveyDetailsDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .publicState(survey.getPublicState())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .creator(survey.getCreator().getUsername())
                .category(survey.getCategory())
                .keywords(survey.getKeywords())
                .build();
    }




    // Search surveys with keyword
    @GetMapping("/search")
    public ResponseEntity<List<Survey>> searchSurveys(@RequestParam("keyword") String keyword) {
        System.out.println("searchSurveys() called");
        System.out.println("keyword is: " + keyword);

        List<Survey> surveys = surveyService.searchSurveys(keyword);
        return ResponseEntity.ok(surveys);
    }

//    String gpt_API_KEY = "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";
    String gpt_API_KEY = "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";
    @GetMapping("/survey/chatbot")
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
