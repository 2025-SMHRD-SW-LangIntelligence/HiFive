package com.smhrd.gitest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper; // JSON 변환을 위해 추가

    public StoreServiceImpl(StoreRepository storeRepository, ObjectMapper objectMapper) {
        this.storeRepository = storeRepository;
        this.objectMapper = objectMapper;
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

    // ★★★ 최종 추천 로직 구현 ★★★
    // ★★★ 최종 추천 로직 구현 ★★★
    @Override
    public List<StoreDto> recommendByFilters(List<String> categories, List<String> addressTags) {
        
        // ★★★ JSON 변환 로직을 완전히 삭제합니다. ★★★
        
        // 1. Repository의 새로운 동적 쿼리를 직접 호출합니다.
        List<Long> recommendedStoreIds = storeRepository.findRecommendedStoreIdsByFilters(categories, addressTags);

        if (recommendedStoreIds.isEmpty()) {
            return List.of();
        }

        // 2. ID 목록으로 실제 가게 정보(Entity)를 조회합니다.
        List<StoreEntity> recommendedStores = storeRepository.findAllById(recommendedStoreIds);

        // 3. (중요) DB에서 조회된 순서(추천 점수 순)를 유지하며 DTO로 변환합니다.
        Map<Long, StoreEntity> storeMap = recommendedStores.stream()
                .collect(Collectors.toMap(StoreEntity::getStoreId, Function.identity()));

        return recommendedStoreIds.stream()
                .map(storeMap::get)
                .filter(java.util.Objects::nonNull)
                .map(StoreDto::fromEntity)
                .collect(Collectors.toList());
    }
}
