package com.smhrd.gitest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.gitest.entity.ReviewEntity;
import com.smhrd.gitest.repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService{
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Override
	public List<ReviewEntity> findMyReviews(String email){
		return reviewRepository.findAllByMember_Email(email);
	}

}
