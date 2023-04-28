package kakaq_be.kakaq_be.test;

import jakarta.servlet.http.HttpServletRequest;
import kakaq_be.kakaq_be.user.domain.User;
import kakaq_be.kakaq_be.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HttpControllerTest {

    @Autowired UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/user/register")
    public String signup(@RequestBody User rq_user){
        try {
            System.out.println(rq_user);
            String rawPassword = rq_user.getPassword();
            String encPassword = bCryptPasswordEncoder.encode(rawPassword);
            rq_user.setPassword(encPassword);
            User new_user = new User(rq_user.getId(), rq_user.getUsername(), rq_user.getPassword(), rq_user.getEmail());
            userRepository.save(new_user);
            return "/login";
        } catch (DataIntegrityViolationException e) {
            return "/duplicate";
        }
    }

    @PostMapping("/user/login")
    public String login(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        System.out.println("로그인 요청");
        Optional<User> userEntityWrapper = userRepository.findByEmail(email);
        User userEntity = userEntityWrapper.orElseThrow(
                ()->new UsernameNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));

        if (bCryptPasswordEncoder.matches(password,userEntity.getPassword())){
            System.out.println(userEntity.getUsername()+"로그인성공");
            return userEntity.getUsername()+"/home";
        }else{
            System.out.println("로그인실패");
            return "/login";
        }
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
