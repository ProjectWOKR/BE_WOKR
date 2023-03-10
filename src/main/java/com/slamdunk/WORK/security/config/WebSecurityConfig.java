package com.slamdunk.WORK.security.config;

import com.slamdunk.WORK.security.jwt.AuthenticationEntryPointHandler;
import com.slamdunk.WORK.security.jwt.JwtAuthenticationFilter;
import com.slamdunk.WORK.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;


    // 정적인 파일에 대한 요청들
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/file/**",
            "/image/**",
            "/swagger/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            // other public endpoints of your API may be appended to this array
            "/h2/**",
            "/h2-console/**"
    };

    @Bean
    public BCryptPasswordEncoder encodePassword() {  // 회원가입 시 비밀번호 암호화에 사용할 Encoder 빈 등록
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable().authorizeRequests();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                // login 없이 접근 허용 하는 url
                .antMatchers("/").permitAll()
                .antMatchers("/api/user/signup").permitAll()
                .antMatchers("/api/user/email").permitAll()
                .antMatchers("/api/user/login").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/css/**").permitAll()
                // '/admin'의 경우 ADMIN 권한이 있는 사용자만 접근이 가능
//                .antMatchers("/admin").hasRole("ADMIN")

                // 그 외 모든 요청은 인증과정 필요
                .anyRequest().authenticated()
                .and()

                //토큰 예외 처리
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPointHandler())
                .and()

                // 토큰 기반 인증이기 때문에 session 사용 x
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // JwtAuthenticationFilter 는 UsernamePasswordAuthenticationFilter 전에 넣음
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 정적인 파일 요청에 대해 무시
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }
}