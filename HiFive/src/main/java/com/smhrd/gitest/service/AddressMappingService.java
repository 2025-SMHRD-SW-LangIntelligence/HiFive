package com.smhrd.gitest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smhrd.gitest.entity.DongMappingEntity;
import com.smhrd.gitest.repository.DongMappingRepository;

import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AddressMappingService {
	
	private final DongMappingRepository dongMappingRepository;
	/**
     * 사용자가 선택한 행정동 리스트를 DB 검색에 사용할 법정동 리스트로 변환합니다.
     * @param selectedAdminDongs 사용자가 프론트에서 선택한 행정동 리스트 (예: ["산수동", "양림동"])
     * @return 검색 쿼리의 WHERE IN 절에 들어갈 법정동 리스트 (예: ["산수1동", "산수2동", "양림동"])
     */
    public List<String> getLegalDongsForSearch(List<String> selectedAdminDongs) {
        if (selectedAdminDongs == null || selectedAdminDongs.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        // DB에서 행정동에 매칭되는 모든 법정동 정보를 가져옴
        List<DongMappingEntity> mappings = dongMappingRepository.findByAdminDongIn(selectedAdminDongs);

        // 결과에서 법정동 이름만 추출하여 리스트로 만듦
        List<String> legalDongs = mappings.stream()
                                          .map(DongMappingEntity::getLegalDong)
                                          .distinct() // 중복 제거
                                          .collect(Collectors.toList());
        
        // 만약 매핑 테이블에 없는 동이 선택될 경우(오타 등), 원래 동 이름도 포함시켜줄 수 있음 (선택사항)
        // 하지만 지금 구조에서는 매핑 테이블 기반으로만 동작하는 것이 더 명확함.

        return legalDongs;
    }
	
}
