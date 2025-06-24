package com.smhrd.gitest.repository;

import com.smhrd.gitest.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// ★★★ StoreRepositoryCustom 인터페이스를 상속받도록 수정 ★★★
@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long>, StoreRepositoryCustom {

    List<StoreEntity> findByShopNameContaining(String keyword);
    List<StoreEntity> findByAddressContaining(String keyword);
    List<StoreEntity> findTop5ByOrderByStoreIdAsc();
 // 법정동 리스트를 받아 IN 절로 검색하는 메서드 추가
    List<StoreEntity> findByLegalDongIn(List<String> legalDongs);
    
    // ★★★ 이전의 복잡한 네이티브 쿼리는 여기서 완전히 삭제합니다. ★★★
}