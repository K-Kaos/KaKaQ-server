package kakaq_be.kakaq_be.user.Repository;

import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.user.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
//    public boolean ExistsByUserEmailANDUserPassword(String userEmail, String userpassword);

    // get surveys that the userId == survey's participant
    @Query("SELECT s FROM Survey s JOIN s.participants p WHERE p.id = :userId")
    List<Survey> findSurveysByUserId(@Param("userId") Long userId);
}
