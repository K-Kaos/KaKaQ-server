package kakaq_be.kakaq_be.survey.domain;

import jakarta.persistence.*;
import kakaq_be.kakaq_be.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.security.Timestamp;
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
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "survey_id" )
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @ElementCollection
    private List<String> keyword;

    @ManyToOne
    private User creator;

    @CreationTimestamp
    private Timestamp startDate;

    @CreationTimestamp
    private Timestamp endDate;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "survey")
    private Set<Question> questions;

    @ManyToMany
    private Set<User> participants;

    @OneToMany(mappedBy = "survey")
    private List<Response> responses;

}
