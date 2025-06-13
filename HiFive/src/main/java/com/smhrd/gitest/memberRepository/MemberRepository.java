package com.smhrd.gitest.memberRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smhrd.gitest.Entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

	
	boolean existsByEmail(String email);
	//로그인 기능
		MemberEntity findAllByEmailAndPw(String email, String pw);
		
	//닉네임 중복 확인
		boolean existsByNickname(String nickname);
}
