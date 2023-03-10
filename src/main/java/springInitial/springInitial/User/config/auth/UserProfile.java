package springInitial.springInitial.User.config.auth;

import lombok.Getter;
import lombok.Setter;
import springInitial.springInitial.User.User;

@Getter
@Setter
public class UserProfile {
    private String userName;
    private String userEmail;

    private String platformType;
  //  private String password;

    public User toUser(){
        return User.builder()
                .userName(userName)
                .userEmail(userEmail)
              //  .password(password)
                .platformType(platformType)
                .build();
    }
}
