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
