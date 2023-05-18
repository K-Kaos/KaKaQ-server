package kakaq_be.kakaq_be.survey.Repository;

import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.user.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findAllByCreator(User creator);
    public Optional<Survey> findSurveyById(Long id);
    Survey findById(long id);

    //공개 설문 찾기
    List<Survey> findAllByPublicState(String public_state);

    //정확하게 키워드가 포함되게 하려면 findBy, 포함된 모든 검색을 하려면 findByContaining
    List<Survey> findByKeyword_KeywordContainingIgnoreCaseAndPublicState(String keyword, String publicState);


}

