package com.smhrd.gitest.memberRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smhrd.gitest.Entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

	
	boolean existsByid(String email);
	//로그인 기능
		MemberEntity findAllByIdAndPw(String email, String pw);
}
