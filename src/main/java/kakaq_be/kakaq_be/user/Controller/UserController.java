package kakaq_be.kakaq_be.user.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.user.Domain.User;
import kakaq_be.kakaq_be.user.Repository.UserRepository;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    String gpt_API_KEY = "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";
    @GetMapping("/user/pt")//심리유형 테스트 시작
    public String ptTest(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + gpt_API_KEY);
        //이 아래 url로 모델 바꿀수 있음.
        String url = "https://api.openai.com/v1/engines/text-davinci-003/completions";

        // 보낼 본문 데이터 설정
        String prompt = "예/아니오로 대답 가능한 심리유형질문을 만들어 주는데 번호는 순서대로 q1.외향적인 사람, q2.내향적인 사람, q3.계획적인 사람, q4.즉흥적인 사람, q5.감정적인 사람, q6.사고적인 사람 임을 알 수 있도록 만들어주고 맞음은 각 질문의 사람에 가깝도록 해줘. 질문만 써주고 질문마다 줄바꿈 한번씩만 해줘";
        System.out.println(prompt);
        String requestBody = "{\"prompt\":\"" + prompt + "\",\"max_tokens\":3500,\"temperature\":0.7}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // API에 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println(response.getBody());

        return response.getBody();
    }
    @RequestMapping("/user/register")//나중에 유형 결과도 여기에 추가해야함.
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

    @GetMapping("/user/{userId}/surveys")
    public List<Survey> getSurveyByUserId(@PathVariable Long userId) {
        return userRepository.findSurveysByUserId(userId);
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
            System.out.println(userEntity.getUsername()+"님, 로그인성공");
            return userEntity.getUsername()+"/home";
        }else{
            System.out.println("로그인실패");
            return "/login";
        }
    }



}
