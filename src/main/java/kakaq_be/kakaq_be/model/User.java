package kakaq_be.kakaq_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String role;

    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public User(int id, String username, String password, String email){
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = "student";
        this.createDate = new Timestamp(System.currentTimeMillis());
    }
//    @Builder
//    public User(String email, String password){
//        this.id = 0;
//        this.username = null;
//        this.password = password;
//        this.email = email;
//        this.role = "student";
//        this.createDate = new Timestamp(System.currentTimeMillis());
//
//    }

}
