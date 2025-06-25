package com.smhrd.gitest.repository;


	

	import com.smhrd.gitest.entity.StoreTagEntity; // ★★★ 1. 관리할 엔티티를 정확히 import 합니다.
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.query.Param;
	import org.springframework.stereotype.Repository;

	import java.util.List;

	@Repository
	// ★★★ 2. JpaRepository<[관리할 엔티티 이름], [ID 타입]> 을 정확히 지정합니다.
	public interface StoreTagRepository extends JpaRepository<StoreTagEntity, Long> {

	    // ★★★ 3. 우리가 그토록 필요했던, 새로운 기능을 여기에 추가합니다! ★★★
	    /**
	     * 특정 가게 ID(storeId)에 연결된 모든 태그의 '이름(tagName)'을 조회하는 쿼리입니다.
	     * store_tag 테이블과 mood_tag 테이블을 조인하여 결과를 가져옵니다.
	     * @param storeId 가게의 ID
	     * @return 태그 이름들의 리스트 (예: ["분위기", "데이트", "와인"])
	     */
	    @Query("SELECT mt.tagName FROM StoreTagEntity st JOIN MoodTagEntity mt ON st.tagId = mt.id WHERE st.storeId = :storeId")
	    List<String> findTagNamesByStoreId(@Param("storeId") Long storeId);

	}

