package kakaq_be.kakaq_be.survey.domain;

import jakarta.persistence.*;
import kakaq_be.kakaq_be.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "response" )
public class Response {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;

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

}
