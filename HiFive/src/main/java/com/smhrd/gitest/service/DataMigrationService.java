package com.smhrd.gitest.service;

import com.smhrd.gitest.entity.DongMappingEntity;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.repository.DongMappingRepository;
import com.smhrd.gitest.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataMigrationService {

    private final StoreRepository storeRepository;
    private final DongMappingRepository dongMappingRepository;

    @Transactional // 여러 데이터를 변경하는 작업이므로 트랜잭션으로 묶어 안전하게 처리합니다.
    public void migrateLegalDongData() {
        System.out.println(">>>>> 법정동 데이터 마이그레이션을 시작합니다...");

        // 1. 우리가 가진 모든 법정동의 '이름' 목록을 가져옵니다. (우리의 사전)
        List<String> allLegalDongs = dongMappingRepository.findAll().stream()
                .map(DongMappingEntity::getLegalDong)
                .distinct()
                .collect(Collectors.toList());

        // 2. legal_dong이 비어있는 모든 가게 목록을 가져옵니다.
        List<StoreEntity> storesToUpdate = storeRepository.findAll().stream()
                .filter(store -> store.getLegalDong() == null || store.getLegalDong().isEmpty())
                .collect(Collectors.toList());
        
        System.out.println(">>>>> 업데이트 대상 가게 수: " + storesToUpdate.size());

        int updatedCount = 0;
        // 3. 각 가게의 'address'를 보면서, 우리 사전에 있는 법정동 이름이 포함되어 있는지 확인합니다.
        for (StoreEntity store : storesToUpdate) {
            String storeAddress = store.getAddress();
            if (storeAddress == null || storeAddress.isEmpty()) {
                continue; // 주소 정보가 없으면 건너뜁니다.
            }

            for (String dongName : allLegalDongs) {
                if (storeAddress.contains(dongName)) {
                    // 4. 주소에서 법정동 이름을 찾았다면, 해당 가게의 legalDong 필드를 채웁니다.
                    store.setLegalDong(dongName);
                    updatedCount++;
                    break; // 하나 찾았으면 다음 가게로 넘어갑니다. (효율성)
                }
            }
        }

        // 5. 변경된 모든 가게 정보를 DB에 한 번에 저장합니다.
        storeRepository.saveAll(storesToUpdate);
        
        System.out.println(">>>>> 총 " + updatedCount + "개의 가게에 법정동 데이터가 업데이트 되었습니다.");
        System.out.println(">>>>> 마이그레이션 완료!");
    }
}
