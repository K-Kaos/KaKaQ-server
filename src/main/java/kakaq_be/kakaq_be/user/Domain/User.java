package kakaq_be.kakaq_be.user.Domain;

import jakarta.persistence.*;
import kakaq_be.kakaq_be.survey.Domain.Question;
import kakaq_be.kakaq_be.survey.Domain.Survey;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "user" )
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( name = "user_id" )
    private int id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String role;

    @CreatedDate
    private Timestamp createDate;

//    @ManyToMany(mappedBy = "participants")
//    private Set<Survey> surveys_participate;
//
//    @OneToMany(mappedBy = "creator")
//    private Set<Survey> surveys_create;

    @Builder
    public User(int id, String username, String password, String email){
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = "student";
        this.createDate = new Timestamp(System.currentTimeMillis());
    }

}
