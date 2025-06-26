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

        // 2. 사용자가 선택한 카테고리에 해당하는 모든 추천 규칙을 DB에서 가져옵니다.
        List<RecommendationRuleEntity> relevantRules = recommendationRuleRepository.findByCategoryIn(userSelectedCategories);
        
        // 3. 빠른 조회를 위해, 규칙을 '태그 이름'을 key로, '가중치'를 value로 하는 Map으로 변환합니다.
        Map<String, Double> tagWeights = relevantRules.stream()
                .collect(Collectors.toMap(
                        RecommendationRuleEntity::getTagName,
                        RecommendationRuleEntity::getWeight,
                        (weight1, weight2) -> weight1 + weight2 // 중복 태그가 있으면 가중치 합산
                ));

        // 4. "필수 태그"에 대한 보너스 점수를 정의합니다.
        final double PRIORITY_SCORE_BONUS = 100.0;

        // 5. 각 후보 가게의 '추천 점수'를 계산합니다.
        List<StoreWithScore> scoredStores = candidateStores.stream().map(store -> {
            double score = 0;
            List<String> storeActualTags = storeTagRepository.findTagNamesByStoreId(store.getStoreId());
            
            // 5-1. 기본 점수를 먼저 계산합니다.
            for (String tag : storeActualTags) {
                score += tagWeights.getOrDefault(tag, 0.0);
            }

            // 5-2. '필수 태그'를 가진 가게에 보너스 점수를 부여합니다.
            if (userSelectedCategories.contains("응원") && storeActualTags.contains("스크린")) {
                score += PRIORITY_SCORE_BONUS;
            }
            if (userSelectedCategories.contains("혼술") && storeActualTags.contains("혼술")) {
                score += PRIORITY_SCORE_BONUS;
            }
            if (userSelectedCategories.contains("특별한 기념일") && storeActualTags.contains("와인")) {
                score += PRIORITY_SCORE_BONUS;
            }
            // (필요 시 다른 보너스 규칙 추가)
            
            return new StoreWithScore(store, score);
        }).collect(Collectors.toList());

        // 6. 계산된 점수가 높은 순으로 가게들을 정렬하고, 점수가 0인 가게는 제외합니다.
        List<StoreEntity> sortedStores = scoredStores.stream()
                .filter(s -> s.getScore() > 0)
                .sorted(Comparator.comparingDouble(StoreWithScore::getScore).reversed())
                .map(StoreWithScore::getStore)
                .collect(Collectors.toList());

        // 7. 최종적으로 정렬된 가게 목록을, 화면에 보여줄 DTO로 변환합니다.
        return sortedStores.stream().map(store -> {
            List<String> storeActualTags = storeTagRepository.findTagNamesByStoreId(store.getStoreId());
            
            // 7-1. 추천의 이유가 된 모든 태그(기본 점수 + 보너스 점수) 목록을 만듭니다.
            Set<String> allRelevantTags = new HashSet<>(tagWeights.keySet());
            if (userSelectedCategories.contains("응원")) allRelevantTags.add("스크린");
            if (userSelectedCategories.contains("혼술")) allRelevantTags.add("혼술");
            if (userSelectedCategories.contains("특별한 기념일")) allRelevantTags.add("와인");

            // 7-2. 가게의 실제 태그가 위 목록에 포함되는 것만 필터링하여 matchedTags를 생성합니다.
            List<String> matchedTags = storeActualTags.stream()
                    .filter(allRelevantTags::contains)
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