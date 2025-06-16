package com.smhrd.gitest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.gitest.entity.StoreEntity;
@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long>{
	// 예: 사용자 위치/감정에 따른 커스텀 쿼리 메서드 추가 가능
	// 술집 이름으로 검색 (부분 일치)
	List<StoreEntity> findByStoreNameContaining(String name);

	// 위치(지역)로 검색 (예: "강남구")
	List<StoreEntity> findByStoreLocationContaining(String location);
	
	// 최신순/오래된순 등 정렬 기준은 프로젝트 성격에 맞게
	List<StoreEntity> findTop5ByOrderByStoreIdAsc(); 
}
