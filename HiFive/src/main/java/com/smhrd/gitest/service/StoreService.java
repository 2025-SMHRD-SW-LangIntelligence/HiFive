package com.smhrd.gitest.service;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.entity.StoreEntity;
import java.util.List;
import java.util.Optional;

public interface StoreService {

    Optional<StoreEntity> findStoreById(Long storeId);
    
    List<StoreDto> getTopRatedStores(int limit);
    
    // ★★★ 최종 추천 메소드 선언 ★★★
    List<StoreDto> recommendByFilters(List<String> categories, List<String> addressTags);
}