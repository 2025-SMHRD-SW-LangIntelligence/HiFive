package com.smhrd.gitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.gitest.entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

	
	boolean existsByEmail(String email);
	//로그인 기능
		MemberEntity findAllByEmail(String email);
		
	//닉네임 중복 확인
		boolean existsByNickname(String nickname);
}
