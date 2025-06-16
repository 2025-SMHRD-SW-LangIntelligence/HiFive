package com.smhrd.gitest.dto;

import com.smhrd.gitest.entity.StoreEntity;
import lombok.Data;

@Data
public class StoreDto {

    private Long storeId;
    private String storeName;
    private String storeLocation;
    private String storeOwner;

    // 필요한 경우 추가 필드 (예: 거리, 감정 태그 등)

    // Entity → DTO 변환
    public static StoreDto fromEntity(StoreEntity entity) {
        StoreDto dto = new StoreDto();
        dto.setStoreId(entity.getStoreId());
        dto.setStoreName(entity.getStoreName());
        dto.setStoreLocation(entity.getStoreLocation());
        dto.setStoreOwner(entity.getStoreOwner());
        return dto;
    }
}