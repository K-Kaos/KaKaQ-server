package kakaq_be.kakaq_be.survey.Domain;

import jakarta.persistence.*;
import kakaq_be.kakaq_be.user.Domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Builder
@Entity
@Table( name = "response" )
public class Response {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long response_id;

//    @Column( name = "fill_date", insertable = false)
//    private Date fillDate;

    @Column(nullable = false, length = 100)
    private String text;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Survey survey;

    @ManyToOne
    private User user;

    @Builder
    public Response(Long response_id, String text, Question question, Survey survey, User user){
        this.response_id = response_id;
        this.text = text;
        this.question = question;
        this.survey = survey;
        this.user = user;
    }



}
