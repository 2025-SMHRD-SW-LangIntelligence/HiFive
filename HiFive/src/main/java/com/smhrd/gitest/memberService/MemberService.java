package com.smhrd.gitest.memberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.smhrd.gitest.Entity.MemberEntity;
import com.smhrd.gitest.memberRepository.MemberRepository;

@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepository;

	public String register(MemberEntity entity) {

		MemberEntity e = memberRepository.save(entity);
		
		if (e != null) {
			// 회원가입 성공
			return "Success";
		} else {
			return "fail";
		}
	}

	// 아이디 중복체크 기능
	public boolean check(String email) {
		return memberRepository.existsByid(email);
	}

	// 로그인 기능
	public MemberEntity login(String email, String pw) {

		return memberRepository.findAllByIdAndPw(email, pw);

	}
	}

