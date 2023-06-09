package kakaq_be.kakaq_be.user.Repository;

import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.user.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
//    public boolean ExistsByUserEmailANDUserPassword(String userEmail, String userpassword);

    // get logined user's participated surveys
    @Query("SELECT s FROM Survey s JOIN s.participants p WHERE p.id = :userId")
    List<Survey> findSurveysByUserId(@Param("userId") Long userId);

    public Optional<User> findByEmail(String email);

    public Optional<User> findById(Long id);

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    public User getUserById(Long id){
//        String sql = "Select * from user Where id=?";
//        User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserMapper());
//        return user;
//    }
//}
    //mypage용 name으로 찾기
    Optional<User> findByUsername(String username);
}
