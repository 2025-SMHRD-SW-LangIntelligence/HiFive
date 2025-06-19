package com.smhrd.gitest.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.smhrd.gitest.entity.MemberEntity;
import com.smhrd.gitest.repository.MemberRepository;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    

    public String register(MemberEntity entity) {
        if (memberRepository.existsByEmail(entity.getEmail())) {
            return "duplicate"; // 이메일 중복일 때 바로 반환
        }
        MemberEntity e = memberRepository.save(entity);
        if (e != null) {
            return "success"; // 저장 성공
        }
        return "fail"; // 저장 실패 등 모든 나머지 경우의 디폴트 반환
        
    }
    	
    public Optional<String> findEmailByNicknameAndBirthdate(String nickname, LocalDate birthdate) {
    	return memberRepository.findByNicknameAndBirthdate(nickname, birthdate)
    			.map(MemberEntity::getEmail); // 찾은 MemberEntity에서 email만 추출
    }
       
    

    // 아이디(이메일) 중복 체크
    public boolean check(String email) {
        return memberRepository.existsByEmail(email);
    }

    
    public MemberEntity login(String email, String pw) {
        MemberEntity member = memberRepository.findByEmail(email).orElse(null);
        if (member != null && member.getPw().equals(pw)) { // 암호화 안 쓸 때
            return member;
        }
        return null;
    }
    
}
