package com.smhrd.gitest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.gitest.entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

	
	
	//로그인 기능
	Optional<MemberEntity> findByEmail(String email);
		
	//닉네임,이메일 중복 확인
		boolean existsByNickname(String nickname);
		
		boolean existsByEmail(String email);
		
	// 회원 등급별 조회
		List<MemberEntity> findAllByGrade(String grade);
}	
