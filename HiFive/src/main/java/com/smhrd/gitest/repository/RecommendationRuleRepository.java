package com.smhrd.gitest.repository;

import com.smhrd.gitest.entity.RecommendationRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRuleRepository extends JpaRepository<RecommendationRuleEntity, Long> {

    // ★★★ (핵심!) 이 메소드를 새로 추가하거나, 기존 파일에 붙여넣으세요. ★★★
    /**
     * 여러 카테고리 이름들을 받아, 해당 카테고리에 속한 모든 '태그 이름'들을 중복 없이 조회합니다.
     * @param categories 사용자가 선택한 카테고리 리스트
     * @return 추천 규칙에 포함된 태그 이름 리스트
     */
    @Query("SELECT DISTINCT r.tagName FROM RecommendationRuleEntity r WHERE r.category IN :categories")
    List<String> findTagNamesByCategories(@Param("categories") List<String> categories);
    
 // ★★★ (핵심!) 카테고리 목록으로 모든 규칙을 통째로 가져오는, 더 강력한 메소드 추가 ★★★
    List<RecommendationRuleEntity> findByCategoryIn(List<String> categories);

}