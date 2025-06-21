package com.smhrd.gitest.service;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.repository.StoreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Optional<StoreEntity> findStoreById(Long storeId) {
        return storeRepository.findById(storeId);
    }

    @Override
    public List<StoreDto> getTopRatedStores(int limit) {
        // 이 로직은 파이썬 점수 테이블을 사용하거나,
        // 지금처럼 단순히 ID 순으로 상위 N개를 가져오는 방식으로 사용할 수 있습니다.
        return storeRepository.findTop5ByOrderByStoreIdAsc().stream()
                .map(StoreDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ★★★ (최종 추천 로직) ★★★
    @Override
    public List<StoreDto> recommendByFilters(List<String> categories, List<String> addressTags) {
        
        List<StoreEntity> initialRecommendations;

        // 1. 카테고리(감정/상황) 기반으로 1차 추천 목록 생성
        if (categories == null || categories.isEmpty()) {
            // 선택된 카테고리가 없으면, 전체 가게를 대상으로 함
            initialRecommendations = storeRepository.findAll();
        } else {
            // 카테고리 기반 추천 쿼리 실행
            List<Long> storeIds = storeRepository.findRecommendedStoreIdsByCategories(categories);
            initialRecommendations = storeRepository.findAllById(storeIds);
        }

        // 2. 주소 태그로 2차 필터링
        List<StoreEntity> finalRecommendations;
        if (addressTags == null || addressTags.isEmpty()) {
            // 선택된 주소가 없으면 1차 추천 목록을 그대로 사용
            finalRecommendations = initialRecommendations;
        } else {
            // 1차 추천 목록에서, 주소가 일치하는 가게만 필터링
            finalRecommendations = initialRecommendations.stream()
                .filter(store -> addressTags.stream()
                                            .anyMatch(tag -> store.getAddress().contains(tag)))
                .collect(Collectors.toList());
        }

        // 3. 최종 결과를 DTO로 변환하여 반환
        // (만약 순서 보정이 필요하다면, 이전 답변의 Map을 사용한 순서 보정 로직을 여기에 추가)
        return finalRecommendations.stream()
                .map(StoreDto::fromEntity)
                .collect(Collectors.toList());
    }
}