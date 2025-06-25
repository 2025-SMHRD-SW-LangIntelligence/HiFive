package com.smhrd.gitest.dto;

import java.util.List;

import com.smhrd.gitest.entity.StoreEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 검색 결과로 프론트엔드에 보내줄 가게의 요약 정보를 담는 DTO 입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {

    private Long storeId;
    private String shopName;
    private String cleanAddress; // 또는 legalDong 등 요약된 주소
    private String photoUrl;
    private Double rating;// 대표 이미지 URL
    private List<String> matchedTags; // 매칭된 태그를 담을 리스트 필드 추가!
    // 필요하다면 다른 요약 정보(평점 등)를 추가할 수 있습니다.

    // StoreEntity를 StoreResponseDto로 변환하는 정적 팩토리 메소드 (서비스 로직에서 사용)
    public static StoreResponseDto fromEntity(StoreEntity entity) {
        return new StoreResponseDto(
            entity.getStoreId(),
            entity.getShopName(),
            entity.getCleanAddress(), // 실제 Entity의 필드명에 맞게 수정
            entity.getPhotoUrl(), // 실제 Entity의 필드명에 맞게 수정
            entity.getRating(),
            null // matchedTags는 나중에 채워야 하기에 일단 오류가 뜨지 않게 null값 입력
        );
    }
}