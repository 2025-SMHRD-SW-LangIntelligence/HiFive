package com.smhrd.gitest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.gitest.entity.ReviewEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{
// 이메일로 내가 쓴 리뷰 전체 조회
	List<ReviewEntity> findAllByMember_Email(String email);
}
