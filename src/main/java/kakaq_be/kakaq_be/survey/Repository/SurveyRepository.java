package kakaq_be.kakaq_be.survey.Repository;

import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.user.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findAllByCreator(User creator);
    public Optional<Survey> findSurveyById(Long id);
    Survey findById(long id);

    //공개 설문 찾기
    List<Survey> findAllByPublicState(String public_state);

    // Search surveys with keyword
    List<Survey> findByKeywordsContaining(String keyword);

    List<Survey> findByTitleAndKeywordsContainingAndCategory(String title, String keyword, String category);

}

