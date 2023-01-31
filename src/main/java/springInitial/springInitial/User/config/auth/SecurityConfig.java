package springInitial.springInitial.User.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity //Spring Security 설정을 활성화시켜주는
@RequiredArgsConstructor // final 필드 생성자 만들어줌
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final OAuthService oAuthService;

    protected void configure(HttpSecurity http) throws  Exception{
        http
                .csrf().disable() //csrf 공격을 막아주는 옵션을 disable
                .headers().frameOptions().disable()
                .and()
                .logout().logoutSuccessUrl("/") //로그아웃 요청시 홈으로 이동

                .and()
                .oauth2Login()//OAuth2 로그인 설정 시작
                .defaultSuccessUrl("/oauth/loginInfo",true)
                .userInfoEndpoint()
                .userService(oAuthService); //OAuth2 로그인 성공 시, 작업을 진행할 userService
    }

/*    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .anyRequest().permitAll();

        httpSecurity
                .formLogin()
                .loginProcessingUrl("/login")
                .loginProcessingUrl("/api/login")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .and()
                .csrf().disable();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }*/
}
