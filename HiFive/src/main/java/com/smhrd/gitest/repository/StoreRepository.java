package com.smhrd.gitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.gitest.entity.StoreEntity;
@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long>{
	// 예: 사용자 위치/감정에 따른 커스텀 쿼리 메서드 추가 가능
}
