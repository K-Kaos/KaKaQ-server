package kakaq_be.kakaq_be.survey.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kakaq_be.kakaq_be.user.Domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "survey" )
public class Survey {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "survey_id" )
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column
    private String category;

    @Column(nullable = false)
    private String publicState;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date startDate;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date endDate;

    // survey status 자동갱신 함수
    public void updateStatusIfExpired() {
        if (this.endDate.before(new Date())) {
            this.status = "false";
        }
    }

    @Column
    private String city;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.REMOVE)
    private List<Question> questions = new ArrayList<>();

    @ManyToMany(mappedBy = "ptSurveys")
    private Set<User> participants;

    @OneToMany(mappedBy = "survey")
    private List<Response> responses;

    @Builder
    public Survey(Long survey_id, String title, String category, String city, Date start_date, Date end_date, String publicState, User creator, List<String> keywords){
        this.id = survey_id;
        this.title = title;
        this.category = category;
        this.city = city;
        this.endDate = end_date;
        this.startDate = start_date;
        this.publicState = publicState;
        this.creator = creator;
        this.status = "true"; // timestamp 보고 status를 자동으로 setting 되게끔 고쳐야함.
        this.keywords = keywords; // FIX: Change 'keyword' to 'keywords'
    }


    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    @ElementCollection
    @CollectionTable(name = "keywords", joinColumns = @JoinColumn(name = "survey_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    public List<String> getKeywords() { // FIX: Change 'getKeyword' to 'getKeywords'
        return keywords;
    }

    public void setKeywords(List<String> keywords) { // FIX: Add this setter method if it doesn't exist already
        this.keywords = keywords;
    }
}

//    public User getCreator(){return creator;}