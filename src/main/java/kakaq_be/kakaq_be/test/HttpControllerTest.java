package kakaq_be.kakaq_be.test;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpServletRequest;
import kakaq_be.kakaq_be.model.User;
import kakaq_be.kakaq_be.repository.UserRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class HttpControllerTest {

    @Autowired UserRepository userRepository;
    @RequestMapping("/user/register")
    public int signupTest(@RequestBody User rq_user){
        System.out.println(rq_user);
        User new_user = new User(rq_user.getId(), rq_user.getUsername(), rq_user.getPassword(), rq_user.getEmail());
        System.out.println(new_user);
        userRepository.save(new_user);
        return 0;
    }

    @RequestMapping("/mypage/gps")
    public String sendGPS(@RequestBody Map<String, Double> gpsData){
        Double latitude = gpsData.get("latitude");
        Double longitude = gpsData.get("longitude");
        String addr = "https://dapi.kakao.com/v2/local/geo/coord2address.json?";
        addr= addr+"x=" +longitude +"&y="+latitude;

        try {
            //위의 주소를 가지고 URL 객체를 생성

            URL url = new URL(addr);

            //URL 객체를 가지고 HttpURLConnection 객체 만들기

            HttpURLConnection con = (HttpURLConnection)url.openConnection();



            //인증부분은 받아야 하면 api에 작성되어있습니다.

            //인증받기

            con.setRequestProperty("Authorization", "카카오 api 자리");

            //옵션 설정

            con.setConnectTimeout(20000);

            con.setUseCaches(false);

            //줄단위 데이터 읽기

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));



            StringBuilder sb = new StringBuilder();

            while(true) {

                String line =br.readLine();

                if(line ==null) {

                    break;

                }

                //읽은 데이터가 있으면 sb에추가

                sb.append(line);

            }

            br.close();

            con.disconnect();



            System.out.println(sb);

            JSONObject obj = new JSONObject(sb.toString());

            System.out.println(obj);

            JSONArray imsi = obj.getJSONArray("documents");

            System.out.println(imsi);

            JSONObject o = imsi.getJSONObject(0);

            System.out.println(o);

            JSONObject c = o.getJSONObject("address");

            String address= c.getString("address_name");



            return address;

        }catch (Exception e) {

            System.out.println("지도보이기" + e.getMessage());

            e.printStackTrace();

        }

        return "answer";
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
