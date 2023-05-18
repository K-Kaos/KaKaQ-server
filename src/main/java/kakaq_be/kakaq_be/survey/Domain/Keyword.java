package kakaq_be.kakaq_be.survey.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "Keyword" )
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Column(nullable = false)
    private String keyword;

    @Builder
    public Keyword(String keyword) {
        this.keyword = keyword;
    }
}
