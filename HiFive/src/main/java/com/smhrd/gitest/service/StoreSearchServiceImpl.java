package com.smhrd.gitest.service;

import com.smhrd.gitest.dto.StoreResponseDto;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.repository.RecommendationRuleRepository; // ★★★ 새로 사용할 Repository import
import com.smhrd.gitest.repository.StoreRepository;
import com.smhrd.gitest.repository.StoreTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreSearchServiceImpl implements StoreSearchService {

    private final StoreRepository storeRepository;
    private final StoreTagRepository storeTagRepository;
    private final RecommendationRuleRepository recommendationRuleRepository; // ★★★ 새로 주입받는 전문가

    @Override
    public List<StoreResponseDto> search(List<String> legalDongs, List<String> userSelectedCategories) {
        
        if (legalDongs == null || legalDongs.isEmpty()) {
            return Collections.emptyList();
        }

        List<StoreEntity> foundStores = storeRepository.findByLegalDongIn(legalDongs);

        // ★★★ (핵심 로직 시작!) ★★★
        // 1. 사용자가 선택한 '카테고리'에 해당하는 모든 '태그 이름'들을 DB에서 가져옵니다.
        List<String> relevantTagNames = recommendationRuleRepository.findTagNamesByCategories(userSelectedCategories);

        List<StoreResponseDto> response = foundStores.stream().map(store -> {
            
            // 2. 이 가게가 실제로 가진 모든 태그 이름을 가져옵니다.
            List<String> storeActualTags = storeTagRepository.findTagNamesByStoreId(store.getStoreId());

            // 3. 이제 '가게의 태그 이름'과, '카테고리에 속한 태그 이름'을 비교합니다.
            List<String> matchedTags = storeActualTags.stream()
                    .filter(tag -> relevantTagNames.contains(tag))
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

        return response;
    }
}