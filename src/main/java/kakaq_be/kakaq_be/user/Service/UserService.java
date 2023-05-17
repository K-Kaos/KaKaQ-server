package kakaq_be.kakaq_be.user.Service;

import kakaq_be.kakaq_be.user.Domain.User;
import kakaq_be.kakaq_be.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserfromSurvey(String email){
        Optional<User> userEntityWrapper = userRepository.findByEmail(email);
        User userEntity = userEntityWrapper.orElseThrow(
                ()->new UsernameNotFoundException("해당 id을 가진 사용자를 찾을 수 없습니다."));
        return userEntity;
    }
}
