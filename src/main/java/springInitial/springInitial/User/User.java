package springInitial.springInitial.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@NoArgsConstructor
@DynamicUpdate //update 할때 실제 값이 변경된 컬럼으로만 update 쿼리를 만듦
@Entity
@Getter
@Table(name = "userTBL")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private Long userID;

    @Column(name = "userEmail", nullable = false)
    private String userEmail;

    @Column(name = "userName", nullable = false, unique = true)
    private String userName;

    @Column(name = "provider")
    private String provider;

    @Column(name = "password")
    private String password;

    @Builder
    public User(Long userID, String userEmail, String userName, String password, String provider){
        this.userID = userID;
        this.userEmail = userEmail;
        this.userName = userName;
        this.password = password;
        this.provider = provider;
    }

    public User update(String userEmail, String userName){
        this.userEmail = userEmail;
        this.userName = userName;
        return this;
    }
}
