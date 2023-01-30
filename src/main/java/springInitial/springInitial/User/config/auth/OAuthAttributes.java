package springInitial.springInitial.User.config.auth;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    GOOGLE("google", (attributes) -> {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserName((String) attributes.get("name"));
        userProfile.setUserEmail((String) attributes.get("email"));
        return userProfile;
    }),

    NAVER("naver",(attributes)->{
       Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        System.out.println("네이버 response: " + response);
        UserProfile userProfile = new UserProfile();
        userProfile.setUserName((String) response.get("name"));
        userProfile.setUserEmail((String) response.get("email"));
        return userProfile;
    }),

    KAKAO ("kakao", (attributes) ->{
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        UserProfile userProfile = new UserProfile();
        userProfile.setUserName((String) kakaoProfile.get("nickname"));
        userProfile.setUserEmail((String) kakaoAccount.get("email"));
        return userProfile;
    });
    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}