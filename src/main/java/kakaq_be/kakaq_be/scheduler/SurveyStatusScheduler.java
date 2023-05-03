package kakaq_be.kakaq_be.scheduler;

import kakaq_be.kakaq_be.survey.Domain.Survey;
import kakaq_be.kakaq_be.survey.Repository.SurveyRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class SurveyStatusScheduler {
    private final SurveyRepository surveyRepository;

    public SurveyStatusScheduler(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정(0초 0분 0시)마다 실행
    public void updateSurveyStatus() {
        List<Survey> surveys = surveyRepository.findAll();
        surveys.forEach(Survey::updateStatusIfExpired);
        surveyRepository.saveAll(surveys);
    }
}

