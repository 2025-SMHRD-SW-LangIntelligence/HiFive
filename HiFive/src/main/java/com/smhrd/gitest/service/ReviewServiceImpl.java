package com.smhrd.gitest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.gitest.entity.MemberEntity;
import com.smhrd.gitest.entity.ReviewEntity;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.repository.ReviewRepository;
import com.smhrd.gitest.repository.StoreRepository;

@Service
public class ReviewServiceImpl implements ReviewService{
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Override
	public List<ReviewEntity> findMyReviews(String email){
		return reviewRepository.findAllByMember_Email(email);	
	
	}
	 @Override
	    public void saveReview(MemberEntity user, Long storeId, String content) {
	        // 1. 리뷰를 작성할 가게 엔티티를 조회
	        StoreEntity store = storeRepository.findById(storeId)
	                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다. id=" + storeId));

	        // 2. 새로운 ReviewEntity 객체 생성
	        ReviewEntity newReview = new ReviewEntity();
	        newReview.setMember(user);      // 작성자 정보 설정
	        newReview.setStore(store);      // 가게 정보 설정
	        newReview.setContent(content);  // 리뷰 내용 설정
	        // newReview.setImage(imageUrl); // 이미지 업로드 기능 추가 시 사용

	        // 3. DB에 저장
	        reviewRepository.save(newReview);
	    }

	   
	 // ★★★ 특정 가게의 모든 리뷰를 찾는 실제 로직 구현 ★★★
	    @Override
	    public List<ReviewEntity> findReviewsByStoreId(Long storeId) {
	        return reviewRepository.findAllByStore_StoreId(storeId);
	    }

}
