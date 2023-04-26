package kakaq_be.kakaq_be.test;

import jakarta.servlet.http.HttpServletRequest;
import kakaq_be.kakaq_be.model.User;
import kakaq_be.kakaq_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Objects;

@RestController
public class HttpControllerTest {
    @GetMapping("/http/get")
    public List <User> getTest(User m) {
        return userRepository.findAll();
    }

    @RequestMapping("/http/post")
    public String postTest(@RequestBody User m) {
        return "post 요청: " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
    }

    @Autowired UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/http/signup")
    public String signUp(@RequestBody User user){
        System.out.println(user);
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        User new_user = new User(user.getId(), user.getUsername(), user.getPassword(), user.getEmail());
        userRepository.save(new_user);
        return "redirect:/login";
    }
    @GetMapping("/signup")
    public String signUp(){
        return "signup";
    }
    String gpt_API_KEY = "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";

    @GetMapping("/http/topic")
//    @SendTo("/topic/public")
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

    //회원가입기능!
//    @RequestMapping("/http/signup")
//    public int signupTest(@RequestBody User rq_user){
//
//        System.out.println(rq_user);
//        User new_user = new User(rq_user.getId(), rq_user.getUsername(), rq_user.getPassword(), rq_user.getEmail());
//        System.out.println(new_user);
//        userRepository.save(new_user);
//        return 0;
//    }

//    @RequestMapping("/http/login")
//    public int loginTest(@RequestBody User temp){
//        System.out.println(temp);
//        User temp_user = new User(temp.getEmail(), temp.getPassword());
//        if(userRepository.ExistsByUserEmailANDUserPassword(temp.getEmail(), temp.getPassword())) return 0;
//        else return 1;
//    }

    @PutMapping("/http/put")
    public String putTest() {
        return "put 요청";
    }

    @DeleteMapping("/http/delete")
    public String deleteTest() {
        return "delete 요청";
    }
}
