package com.smhrd.gitest.service;

import com.smhrd.gitest.dto.StoreResponseDto;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreSearchServiceImpl implements StoreSearchService {

    private final StoreRepository storeRepository;

    @Override
    public List<StoreResponseDto> search(List<String> legalDongs, List<String> moodTags) {
        
        // 1. 파라미터로 받은 법정동 리스트(legalDongs)가 비어있는지 확인합니다.
        // 만약 비어있다면, 검색할 동네가 없다는 뜻이므로 빈 리스트를 반환합니다.
        if (legalDongs == null || legalDongs.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        // 2. (핵심!) storeRepository.findAll() 대신, 우리가 만든 findByLegalDongIn을 호출합니다.
        // 이 메소드는 legalDongs 리스트에 포함된 법정동을 가진 가게만 정확히 찾아옵니다.
        List<StoreEntity> foundStores = storeRepository.findByLegalDongIn(legalDongs);

        // TODO: 3. (다음 단계) 여기서 foundStores를 moodTags 기준으로 한 번 더 필터링하거나,
        // 추천 점수 순으로 정렬하는 로직이 추가되어야 합니다.
        // 지금은 우선 위치 기반 검색만 완벽하게 만듭니다.

        // 4. 검색된 StoreEntity 리스트를 프론트에 보여줄 StoreResponseDto 리스트로 변환합니다.
        List<StoreResponseDto> response = foundStores.stream()
                .map(StoreResponseDto::fromEntity) // 정적 팩토리 메소드 사용
                .collect(Collectors.toList());

        return response;
    }
}