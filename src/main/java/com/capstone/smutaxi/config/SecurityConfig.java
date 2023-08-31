package com.capstone.smutaxi.config;

import com.capstone.smutaxi.config.jwt.JwtAuthenticationFilter;
import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.enums.Role;
import com.capstone.smutaxi.service.user.UserDetailsServiceImpl;
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

                //엔드포인트 접근 권한 관리
                .authorizeRequests()
                //ADMIN 권한을 가지고 있어야 집회정보를 등록하는 api 자원 사용 가능
                .antMatchers("/api/rally-information/create").hasRole(Role.ADMIN.name())
                .antMatchers("/api/auth/grant-admin/{email}").hasRole(Role.ADMIN.name())
                //인증 없이 접근 가능한 엔드포인트들
                .antMatchers("/",
                        "/api/auth/join",
                        "/api/auth/login",
                        "/api/auth/send/verification-email",
                        "/api/auth/check-duplicate/{email}",

                        "/api/users/{email}/password",
                        "/api/images/**",

                        "/api/chatrooms/{roomId}/add-user",
                        "/api/chatrooms",
                        "/api/chatrooms/{roomId}/participants/{participantId}/room-exit-time",
                        "/h2-console/**"
                ).permitAll()

                .anyRequest().authenticated()
                .and()

                // JwtAuthenticationFilter를 먼저 적용
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }


}