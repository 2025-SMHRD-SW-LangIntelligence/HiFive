package com.smhrd.gitest.service;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.entity.StoreEntity;
import java.util.List;
import java.util.Optional;

public interface StoreService {

    /**
     * 가게 ID로 특정 가게의 상세 정보를 조회합니다.
     * @param storeId 가게 ID
     * @return StoreEntity Optional 객체
     */
    Optional<StoreEntity> findStoreById(Long storeId);
    
    /**
     * 메인 페이지에 표시될 기본 추천 가게 목록을 조회합니다. (점수 기반)
     * @param limit 가져올 가게의 수
     * @return StoreDto 리스트
     */
    List<StoreDto> getTopRatedStores(int limit);
    
    /**
     * ★★★ (최종 추천 메소드) ★★★
     * 사용자가 선택한 카테고리(감정/상황)와 주소 태그를 모두 사용하여
     * 가게를 필터링하고 추천합니다. 이 메소드 선언을 여기에 추가해야 합니다.
     * @param categories 선택된 감정/상황 카테고리 리스트
     * @param addressTags 선택된 주소(구/동) 태그 리스트
     * @return 추천된 StoreDto 리스트
     */
    List<StoreDto> recommendByFilters(List<String> categories, List<String> addressTags);
}