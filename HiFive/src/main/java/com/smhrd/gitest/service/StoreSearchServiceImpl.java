package com.smhrd.gitest.service;

import com.smhrd.gitest.dto.StoreResponseDto;
import com.smhrd.gitest.entity.RecommendationRuleEntity;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.repository.RecommendationRuleRepository;
import com.smhrd.gitest.repository.StoreRepository;
import com.smhrd.gitest.repository.StoreTagRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreSearchServiceImpl implements StoreSearchService {

    private final StoreRepository storeRepository;
    private final StoreTagRepository storeTagRepository;
    private final RecommendationRuleRepository recommendationRuleRepository;

    @Override
    public List<StoreResponseDto> search(List<String> legalDongs, List<String> userSelectedCategories) {
        
        if (legalDongs == null || legalDongs.isEmpty() || userSelectedCategories == null || userSelectedCategories.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 위치 기반으로 후보 가게들을 먼저 찾습니다.
        List<StoreEntity> candidateStores = storeRepository.findByLegalDongIn(legalDongs);

        // 2. 사용자가 선택한 카테고리에 해당하는 모든 추천 규칙을 DB에서 '한 번만' 가져옵니다.
        List<RecommendationRuleEntity> relevantRules = recommendationRuleRepository.findByCategoryIn(userSelectedCategories);
        
        // 3. 빠른 조회를 위해, 규칙을 '태그 이름'을 key로, '가중치'를 value로 하는 Map으로 변환합니다.
        Map<String, Double> tagWeights = relevantRules.stream()
                .collect(Collectors.toMap(
                        RecommendationRuleEntity::getTagName,
                        RecommendationRuleEntity::getWeight,
                        (weight1, weight2) -> weight1 + weight2 // 중복된 태그가 있으면 가중치를 합산
                ));

        // 4. 각 후보 가게의 '추천 점수'를 계산합니다.
        List<StoreWithScore> scoredStores = candidateStores.stream().map(store -> {
            double score = 0;
            List<String> storeActualTags = storeTagRepository.findTagNamesByStoreId(store.getStoreId());
            
            // 가게의 실제 태그들이, 우리가 만든 가중치 Map에 있는지 확인하고 점수를 합산합니다.
            for (String tag : storeActualTags) {
                score += tagWeights.getOrDefault(tag, 0.0);
            }
            return new StoreWithScore(store, score);
        }).collect(Collectors.toList());

        // 5. (핵심!) 계산된 점수가 높은 순으로 가게들을 정렬합니다. 점수가 0인 가게는 제외합니다.
        List<StoreEntity> sortedStores = scoredStores.stream()
                .filter(s -> s.getScore() > 0) // 점수가 있는 가게만 필터링
                .sorted(Comparator.comparingDouble(StoreWithScore::getScore).reversed())
                .map(StoreWithScore::getStore)
                .collect(Collectors.toList());

        // 6. 최종적으로 정렬된 가게 목록을 DTO로 변환하여 반환합니다.
        return sortedStores.stream().map(store -> {
            List<String> storeActualTags = storeTagRepository.findTagNamesByStoreId(store.getStoreId());
            List<String> matchedTags = storeActualTags.stream()
                    .filter(tagWeights::containsKey) // 추천 규칙에 포함된 태그만 매칭된 태그로 간주
                    .collect(Collectors.toList());
            
            return new StoreResponseDto(
                store.getStoreId(),
                store.getShopName(),
                store.getCleanAddress(),
                store.getPhotoUrl(),
                store.getRating(),
                matchedTags
            );
        }).collect(Collectors.toList());
    }

    // 점수와 가게를 함께 저장하기 위한 내부 헬퍼 클래스
    @Getter
    private static class StoreWithScore {
        private final StoreEntity store;
        private final double score;

        public StoreWithScore(StoreEntity store, double score) {
            this.store = store;
            this.score = score;
        }
    }
}