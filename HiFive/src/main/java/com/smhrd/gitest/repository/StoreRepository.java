package com.smhrd.gitest.repository;

import com.smhrd.gitest.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    // 1. 가게 이름으로 검색 (부분 일치)
    // ★★★ findByStoreNameContaining -> findByShopNameContaining 으로 수정 ★★★
	// keyword로 담아주는 이유 : 사용자가 입력한 값을 담는 변수이기 떄문!
    List<StoreEntity> findByShopNameContaining(String keyword);

    // 2. 주소로 검색 (부분 일치)
    // ★★★ findByStoreLocationContaining -> findByAddressContaining 으로 수정 ★★★
    List<StoreEntity> findByAddressContaining(String keyword);
    
    // 3. 메인 페이지 Top 5 추천 (ID 순)
    // ★★★ findTop5ByOrderByStoreIdAsc -> findTop5ByOrderByIdAsc 로 수정 (storeId -> id) ★★★
    List<StoreEntity> findTop5ByOrderByStoreIdAsc();
    
 // ★★★ 새로운 추천 시스템 쿼리만 남깁니다 ★★★
    @Query(value =
        "SELECT st.store_id " +
        "FROM store_tag st " +
        "JOIN mood_tag mt ON st.tag_id = mt.tag_id " +
        "JOIN recommendation_rule rr ON mt.tag_name = rr.tag_name " +
        "WHERE rr.category IN (:categories) " +
        "GROUP BY st.store_id " +
        "ORDER BY SUM(rr.weight) DESC " +
        "LIMIT 20",
        nativeQuery = true)
    List<Long> findRecommendedStoreIdsByCategories(@Param("categories") List<String> categories);
}