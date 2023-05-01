package kakaq_be.kakaq_be.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
//      SecuriyFilterChain 사용하여 로그인 처리 실패-!
//        http.authorizeRequests()
//                .requestMatchers("/mypage/**").authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/http/login")
//                .failureHandler(new LoginFailureHandler())
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .defaultSuccessUrl("/");
//        http
//                .logout()
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/");
//
//        System.out.println("logintest");
        return http.build();
    }
}
