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
    @Column(name = "userNum")
    private Long userNum;

    @Column(name = "userID")
    private String userID;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "userName", nullable = false, unique = true)
    private String userName;

    @Column(name = "platformType")
    private String platformType;

    @Column(name = "password")
    private String password;

    @Builder
    public User(Long userNum, String userID, String userEmail, String userName, String password, String platformType){
        this.userNum = userNum;
        this.userID = userID;
        this.userEmail = userEmail;
        this.userName = userName;
        this.password = password;
        this.platformType = platformType;
    }

    public User update(String userEmail, String userName){
        this.userEmail = userEmail;
        this.userName = userName;
        return this;
    }
}
