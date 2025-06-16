package com.smhrd.gitest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.smhrd.gitest.service.MemberUserDetailsService;

@Configuration
public class WebSecurityConfig {
	@Autowired
	private MemberUserDetailsService memberUserDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
         // CSRF 설정 (필요에 따라)
         .csrf().disable()
         // 정적 리소스와 로그인·회원가입 화면은 인증 없이 접근 허용
         .authorizeHttpRequests(auth -> auth
             .requestMatchers(
                 "/css/**", "/js/**", "/images/**",
                 "/login", "/register", "/error",
                 "/"
                 
             ).permitAll()
             .anyRequest().authenticated()
         )
         // 커스텀 로그인 폼 사용
         .formLogin(form -> form
             .loginPage("/login")      // GET 요청 시 내 컨트롤러 매핑
             .loginProcessingUrl("/login")  // POST form action
             .defaultSuccessUrl("/", true)  // 로그인 성공 후 이동
             .failureUrl("/login?error")    // 로그인 실패 시
             .permitAll()
         )
         // 로그아웃 설정
         .logout(logout -> logout
             .logoutUrl("/logout")
             .logoutSuccessUrl("/login?logout")
             .permitAll()
         );
         //.userDetailsService(memberUserDetailsService);

     return http.build();
		
		
}
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
}