package kakaq_be.kakaq_be.survey.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.user.Domain.User;
import kakaq_be.kakaq_be.user.Repository.UserRepository;
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
@RequestMapping("/api/mypage")
public class MyPageController {
    @Value("0fe37deeaccdff24161e7671384de7b9")
    private String apiKey;
    @RequestMapping("/gps")//마이페이지에 현재 유저 위치 전송
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
//    @RequestMapping("/user")
//    public User sendUser(@RequestBody Long userId){
//        System.out.println(UserRepository.findUserById(userId));
//        return UserRepository.findUserById(userId);
//
//    }
//    @RequestMapping("/participate") //참여한 설문조사list 가져오기
//    public List<Survey> getParticipate(){}
//
//    @RequestMapping("/create")//생성한 설문조사list가져오기
//    public List<Survey> getCreate(){}
//
//    @RequestMapping("/result")//해당 설문조사 결과 가져오기
//    public List<Survey> getResult(){}


}
