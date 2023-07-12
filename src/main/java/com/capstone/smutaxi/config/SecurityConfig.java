package com.capstone.smutaxi.config;

import com.capstone.smutaxi.config.jwt.JwtAuthenticationFilter;
import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.service.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private UserDetailsServiceImpl userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //암호화에 필요한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //authenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //인증 후에 권한을 얻을 수 있는 자원과 그렇지 않은 자원을 설정
    //내가 커스터마이즈 한 순서대로 fitering 하게 하는 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //h2 콘솔 사용
                .csrf().disable().headers().frameOptions().disable()
                .and()

                //세션 사용 안함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                //URL 관리
                .authorizeRequests()
                .antMatchers("/",
                        "/api/auth/join",
                        "/api/auth/login",
                        "/api/auth/users/{email}",
                        "/api/auth/users/{email}/password",
                        "/api/auth/join/email-verification",
                        "/api/auth/update/email-verification",
                        "/api/auth/check-duplicate/{email}",

                        "/api/images/profile-image/upload",
                        "/api/images/profile-image/{fileName}",
//                        "/demoImageURL",


                        "/api/rally-info",
                        "/api/rally-info/create",

//                        "/users/update",
                        "/api/chat/add-user",
                        "/api/chat/user/chatRooms",
                        "/h2-console/**",
                        "/ws/**",
                        "/chat/**").permitAll()

                .anyRequest().authenticated()
                .and()

                // JwtAuthenticationFilter를 먼저 적용
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }


}