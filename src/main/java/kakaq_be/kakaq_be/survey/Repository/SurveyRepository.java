package kakaq_be.kakaq_be.survey.Repository;

import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.user.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    //List<Survey> findByUserId(Long userId);
    public Optional<Survey> findSurveyById(Long id);
}

