package com.smhrd.gitest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	
	// ★ 추천 시스템을 위한 네이티브 쿼리 추가
	// 일치하는 태그가 많은 순서로 술집 정렬하는 쿼리문
    @Query(value = "SELECT store_id FROM store_tag WHERE tag_id IN :tagIds GROUP BY store_id ORDER BY COUNT(store_id) DESC LIMIT 20",
           nativeQuery = true)
    List<Long> findStoreIdsByTags(@Param("tagIds") List<Long> tagIds);
}
