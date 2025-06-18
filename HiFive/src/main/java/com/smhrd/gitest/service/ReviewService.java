package com.smhrd.gitest.service;

import java.util.List;

import com.smhrd.gitest.entity.ReviewEntity;

public interface ReviewService {
	// 내가 쓴 리뷰 전체 조회
	List<ReviewEntity> findMyReviews(String email);
}
