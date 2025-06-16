package com.smhrd.gitest.service;
	
	

	import com.smhrd.gitest.entity.MemberEntity;
	import com.smhrd.gitest.repository.MemberRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.security.core.userdetails.User;
	import org.springframework.security.core.userdetails.UserDetails;
	import org.springframework.security.core.userdetails.UserDetailsService;
	import org.springframework.security.core.userdetails.UsernameNotFoundException;
	import org.springframework.stereotype.Service;

	@Service
	public class MemberUserDetailsService implements UserDetailsService {

	    @Autowired
	    private MemberRepository memberRepository;

	    @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        MemberEntity member = memberRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 없습니다: " + email));
	        return User.builder()
	                .username(member.getEmail())
	                .password(member.getPw()) // 암호화된 비밀번호여야 함!
	                .roles(member.getGrade()) // 예: "USER", "ADMIN"
	                .build();
	    }
	}

