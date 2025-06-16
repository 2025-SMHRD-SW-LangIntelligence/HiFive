package com.smhrd.gitest.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smhrd.gitest.entity.MemberEntity;
import com.smhrd.gitest.repository.MemberRepository;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원가입 (비밀번호 암호화)
    public String register(MemberEntity entity) {
    	if (memberRepository.existsByEmail(entity.getEmail())) {
            return "duplicate"; // 이미 등록된 이메일이면 회원가입을 막음.
        }
    	
        entity.setPw(passwordEncoder.encode(entity.getPw()));
        MemberEntity e = memberRepository.save(entity);
        return (e != null) ? "success" : "fail";
    }

    // 아이디(이메일) 중복 체크
    public boolean check(String email) {
        return memberRepository.existsByEmail(email);
    }

    // (로그인 기능은 직접 구현 X, Security에서 UserDetailsService로 처리)
    // 이메일로 회원 조회 등만 필요 시 구현
    public MemberEntity findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }
}
