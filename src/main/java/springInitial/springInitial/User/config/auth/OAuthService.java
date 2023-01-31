package springInitial.springInitial.User.config.auth;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import springInitial.springInitial.User.User;
import springInitial.springInitial.User.UserRepository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    //Spring Security가 access token을 이용해서 OAuth2 Server에서 유저 정보를 가져온 다음 loadUser 메서드를 통해 유저의 정보를 가져온다
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); //Oauth 서비스에서 가져온 유저 정보를 담고 있음
        System.out.println("서비스에서 가져온 유저 정보를 담고 있는 oAuth2User: "+ oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //OAuth 서비스 이름=
        System.out.println("서비스 이름 : " + registrationId);

        //OAuth 로그인 시 키가 되는 값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        System.out.println("userNameAttributeName: " + userNameAttributeName);

        //OAuth 서비스의 유저 정보들
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("attributes: " + attributes);

        // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌
        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);

        userProfile.setPlatformType(registrationId);
        User user = saveOrUpDate(userProfile);

        Map<String, Object> customAttribute = customAttribute(attributes, userNameAttributeName, userProfile,registrationId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                customAttribute,
                userNameAttributeName);

    }

    //필요한 정보만 추출하기 위한
    private Map customAttribute(Map attributes, String userNameAttributeName, UserProfile userProfile, String registrationId){
        Map<String,Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("platformType", registrationId);
        customAttribute.put("userName", userProfile.getUserName());
        customAttribute.put("userEmail", userProfile.getUserEmail());
        return customAttribute;
    }

    //유저 정보 변경을 대비한 DB update 코드
    private User saveOrUpDate(UserProfile userProfile){
        User user = userRepository.findByUserEmailAndPlatformType(userProfile.getUserEmail(),userProfile.getPlatformType())
                .map(m->m.update(userProfile.getUserName(),userProfile.getUserEmail()))
                .orElse(userProfile.toUser());

        return userRepository.save(user);
    }
}
