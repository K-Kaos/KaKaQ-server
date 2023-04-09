package kakaq_be.kakaq_be.repository;

import kakaq_be.kakaq_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
//    public boolean ExistsByUserEmailANDUserPassword(String userEmail, String userpassword);

}
