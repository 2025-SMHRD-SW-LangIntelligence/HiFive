package com.smhrd.gitest.service;

import com.smhrd.gitest.entity.MoodTagEntity;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.entity.StoreTagEntity;
import com.smhrd.gitest.repository.MoodTagRepository;
import com.smhrd.gitest.repository.StoreRepository;
import com.smhrd.gitest.repository.StoreTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataMigrationService {

    private final StoreRepository storeRepository;
    private final MoodTagRepository moodTagRepository;
    private final StoreTagRepository storeTagRepository;
    // (만약 KakaoApiService가 있다면, 그대로 둡니다)
    // private final KakaoApiService kakaoApiService;

    // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
    // ★★★ (핵심!) 이 메소드가 파일에 없었기 때문에 에러가 발생했습니다! ★★★
    // ★★★ 이 메소드를 여기에 새로 추가해주세요.                            ★★★
    // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
    @Transactional
    public void migrateStoreTags() {
        System.out.println(">>>>> 가게-태그 관계 데이터 마이그레이션을 시작합니다...");

        Map<String, Long> tagNameToIdMap = moodTagRepository.findAll().stream()
                .collect(Collectors.toMap(MoodTagEntity::getTagName, MoodTagEntity::getTagId));
        
        System.out.println(">>>>> 전체 태그 수: " + tagNameToIdMap.size());

        List<StoreEntity> allStores = storeRepository.findAll();

        storeTagRepository.deleteAllInBatch();
        System.out.println(">>>>> 기존 store_tag 데이터를 삭제했습니다.");

        int createdCount = 0;
        for (StoreEntity store : allStores) {
            String midTagString = store.getMidTag();
            if (midTagString == null || midTagString.trim().isEmpty()) {
                continue;
            }

            String[] tags = midTagString.split(" ");

            for (String tagName : tags) {
                Long tagId = tagNameToIdMap.get(tagName.trim());
                
                if (tagId != null) {
                    StoreTagEntity newStoreTag = new StoreTagEntity();
                    newStoreTag.setStoreId(store.getStoreId());
                    newStoreTag.setTagId(tagId);
                    storeTagRepository.save(newStoreTag);
                    createdCount++;
                }
            }
        }
        
        System.out.println(">>>>> 총 " + createdCount + "개의 가게-태그 관계가 생성되었습니다.");
        System.out.println(">>>>> 마이그레이션 완료!");
    }

    // (참고) 이전에 만들었던 다른 마이그레이션 메소드가 있다면, 그대로 두시면 됩니다.
    public void migrateLegalDongData() {
        // ... (이전 법정동 마이그레이션 로직) ...
        System.out.println("법정동 마이그레이션은 현재 호출되지 않았습니다.");
    }
}