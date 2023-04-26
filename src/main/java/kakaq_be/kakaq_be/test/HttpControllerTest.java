package kakaq_be.kakaq_be.test;

import kakaq_be.kakaq_be.model.User;
import kakaq_be.kakaq_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
