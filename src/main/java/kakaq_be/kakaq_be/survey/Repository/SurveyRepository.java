package kakaq_be.kakaq_be.survey.Repository;

import kakaq_be.kakaq_be.survey.Domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

}

