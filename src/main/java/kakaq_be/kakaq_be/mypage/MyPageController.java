package kakaq_be.kakaq_be.mypage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakaq_be.kakaq_be.survey.Domain.*;
import kakaq_be.kakaq_be.survey.Dto.*;
import kakaq_be.kakaq_be.survey.Repository.ParticipantRepository;
import kakaq_be.kakaq_be.survey.Repository.ResponseRepository;
import kakaq_be.kakaq_be.survey.Repository.SurveyRepository;
import kakaq_be.kakaq_be.survey.Service.ResponseService;
import kakaq_be.kakaq_be.user.Domain.User;
import kakaq_be.kakaq_be.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mypage")
public class MyPageController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @PostMapping("/userInfo")
    public ResponseEntity<String> getLoggedInUser(@RequestBody Map<String, String> request) {
        String loggedInUser = request.get("user");
        Optional<User> userEntityWrapper = userRepository.findByEmail(loggedInUser);
        User user = userEntityWrapper.orElseThrow(
                ()->new UsernameNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok("{\"username\":\"" + user.getUsername() + "\"}");
    }

    //get logined user's created surveys
    @GetMapping("/created")
    public ResponseEntity<List<Survey>> getCreatedSurveys(@RequestParam String user) {
        Optional<User> userEntityWrapper = userRepository.findByEmail(user);
        User loggedInUser = userEntityWrapper.orElseThrow(
                () -> new UsernameNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다.")
        );
        List<Survey> createdSurveys = surveyRepository.findAllByCreator(loggedInUser);
        return ResponseEntity.ok(createdSurveys);
    }

    // get logined user's participated surveys
    @RequestMapping("/participated") //참여한 설문조사list 가져오기
    public ResponseEntity<List<SurveyDetailsDto>> getParticipatedS(@RequestParam String user){
        Optional<User> userEntityWrapper = userRepository.findByEmail(user);
        User loggedInUser = userEntityWrapper.orElseThrow(
                () -> new UsernameNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다.")
        );
        List<Participant> participants = participantRepository.findByUserId(loggedInUser.getId());
        List<SurveyDetailsDto> participatedSurveys = participants.stream()
                .map(participant -> mapToSurveyDto(participant.getSurvey()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(participatedSurveys);
    }

    private SurveyDetailsDto mapToSurveyDto(Survey survey) {
        return SurveyDetailsDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .creator(survey.getCreator().getUsername())
                .build();
    }

    //get responses for a survey by question
    @GetMapping("/created/{id}/responses")
    public Map<Long, List<ResponseByQuestionDto>> getResponsesByQuestionId(@PathVariable Long id){
        Optional<Survey> surveyEntityWrapper = surveyRepository.findSurveyById(id);
        surveyEntityWrapper.orElseThrow(
                ()->new UsernameNotFoundException("해당 id을 가진 survey를 찾을 수 없습니다."));
        List<Response> responses = responseRepository.findBySurveyId(id);
        Map<Long, List<ResponseByQuestionDto>> responsesByQuestionId = new HashMap<>();

        for (Response response : responses) {
            Long questionId = response.getQuestion().getQuestion_id();
            String questionType = response.getQuestion().getType().getName();
            List<ResponseByQuestionDto> questionResponses = responsesByQuestionId.getOrDefault(questionId, new ArrayList<>());

            if ("객관식".equals(questionType) || "찬부식".equals(questionType)) {
                String option = response.getText();
                String answererRole = response.getUser().getRole();
                incrementOptionCountByRole(questionResponses, option, answererRole);
            }else{
                ResponseByQuestionDto responseDto = new ResponseByQuestionDto();
                responseDto.setValue(response.getText());
                responseDto.setAnswerer(response.getUser().getUsername());
                responseDto.setAnswererRole(response.getUser().getRole());
                questionResponses.add(responseDto);
            }
            responsesByQuestionId.put(questionId, questionResponses);

        }
        return responsesByQuestionId;
    }

    private void incrementOptionCountByRole(List<ResponseByQuestionDto> optionCounts, String option, String answererRole) {
        // 해당 응답 옵션과 답변자 역할에 대한 카운트를 증가시키거나 새로운 카운트 객체를 추가
        for (ResponseByQuestionDto countDto : optionCounts) {
            if (countDto.getValue().equals(option) && countDto.getAnswererRole().equals(answererRole)) {
                countDto.setCount(countDto.getCount() + 1);
                return;
            }
        }
        // 새로운 응답 옵션 카운트 객체 생성
        ResponseByQuestionDto newCountDto = ResponseByQuestionDto.builder()
                .value(option)
                .answererRole(answererRole)
                .count(1L)
                .build();
        optionCounts.add(newCountDto);
    }

    @Value("0fe37deeaccdff24161e7671384de7b9")
    private String apiKey;
    @RequestMapping("/gps")//Get user's current location
    public String sendGPS(@RequestBody  Map<String, Float> gpsData) throws JsonProcessingException {
        Float latitude = gpsData.get("latitude");
        Float longitude = gpsData.get("longitude");

        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" + longitude + "&y=" + latitude;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String result = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(result);
        JsonNode documentsNode = rootNode.get("documents");
        JsonNode firstDocumentNode = documentsNode.get(0);
        JsonNode addressNode = firstDocumentNode.get("address");
        String region1depthName = addressNode.get("region_1depth_name").asText();
        String region2depthName = addressNode.get("region_2depth_name").asText();
        String[] Metropolitan = {"서울", "부산", "대구", "인천", "대전", "광주", "울산", "세종"};
        List<String> MetroCity = new ArrayList<>(Arrays.asList(Metropolitan));
        if(MetroCity.contains(region1depthName)) {
            return region1depthName + "시";
        }
        else return region2depthName;
    }
//    @RequestMapping("/participate") //참여한 설문조사list 가져오기
//    public List<Survey> getParticipate(){}
//
//    @RequestMapping("/create")//생성한 설문조사list가져오기
//    public List<Survey> getCreate(){}
//
//    @RequestMapping("/result")//해당 설문조사 결과 가져오기
//    public List<Survey> getResult(){}


}
