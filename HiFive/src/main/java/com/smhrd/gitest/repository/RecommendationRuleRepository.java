package com.smhrd.gitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.gitest.entity.RecommendationRuleEntity;

@Repository
public interface RecommendationRuleRepository extends JpaRepository<RecommendationRuleEntity, Long>{
	
	// 이 레포지토리는 나중에 규칙을 관리하는 기능(CRUD)을 만들 때 사용할 수 있습니다.
    // 지금 당장은 비어있어도 괜찮습니다.
	
	
	
}
