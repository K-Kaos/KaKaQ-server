package kakaq_be.kakaq_be.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kakaq_be.kakaq_be.model.User;
import kakaq_be.kakaq_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class HttpControllerTest {
//    @Bean
//    public RestTemplate RestTemplate(){
//        return new RestTemplate();
//    }

    @Value("0fe37deeaccdff24161e7671384de7b9")
    private String apiKey;
    @RequestMapping("/mypage/gps")
    public String sendGPS(@RequestBody  Map<String, Float> gpsData) throws JsonProcessingException {
        Float latitude = gpsData.get("latitude");
        Float longitude = gpsData.get("longitude");

        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" + longitude + "&y=" + latitude;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        System.out.println(entity);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String result = response.getBody();
        System.out.println(result);


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(result);

        JsonNode documentsNode = rootNode.get("documents");
        JsonNode firstDocumentNode = documentsNode.get(0);
        JsonNode addressNode = firstDocumentNode.get("address");
        String region1depthName = addressNode.get("region_1depth_name").asText();
        return region1depthName;
    }
    @Autowired UserRepository userRepository;
    @RequestMapping("/user/register")
    public int signupTest(@RequestBody User rq_user){
        System.out.println(rq_user);
        User new_user = new User(rq_user.getId(), rq_user.getUsername(), rq_user.getPassword(), rq_user.getEmail());
        System.out.println(new_user);
        userRepository.save(new_user);
        return 0;
    }
    String gpt_API_KEY = "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";
    @RequestMapping("/survey/create/chatbot")
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
