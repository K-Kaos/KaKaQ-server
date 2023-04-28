package kakaq_be.kakaq_be.user.repository;

import kakaq_be.kakaq_be.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
//    public boolean ExistsByUserEmailANDUserPassword(String userEmail, String userpassword);
    public Optional<User> findByEmail(String email);
}
