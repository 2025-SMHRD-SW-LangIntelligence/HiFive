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
import java.util.HashMap; // ★★★ HashMap import
import java.util.HashSet;
import java.util.List;   // ★★★ List import
import java.util.Map;    // ★★★ Map import
import java.util.Set;
import java.util.stream.Collectors; // ★★★ Collectors import

@Service
@RequiredArgsConstructor
public class DataMigrationService {

    private final StoreRepository storeRepository;
    private final MoodTagRepository moodTagRepository;
    private final StoreTagRepository storeTagRepository;
    // (만약 KakaoApiService가 있다면, 그대로 둡니다)
    // private final KakaoApiService kakaoApiService;

    // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
    // ★★★ (핵심!) 메소드 시그니처(선언부)에 오타가 없는지 꼼꼼히 확인해주세요. ★★★
    // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
    @Transactional
    public void migrateStoreTags() { // public void migrateStoreTags() 가 정확한 형태입니다.
        System.out.println(">>>>> 가게-태그 관계 데이터 대통합 마이그레이션을 시작합니다...");

        // 1. "고대어-현대어 번역 사전"을 정의합니다. (여기에 규칙을 추가/수정할 수 있습니다)
        Map<String, String> keywordMapping = new HashMap<>(); // ★★★ keywordMapping 변수 선언 및 초기화
        keywordMapping.put("스포츠", "스크린");
        keywordMapping.put("대형스크린", "스크린");
        keywordMapping.put("이자카야", "일식");
        keywordMapping.put("감성", "분위기");
        keywordMapping.put("분위기 좋은", "분위기");
        keywordMapping.put("깔끔한", "분위기");
        keywordMapping.put("아늑한", "분위기");
        keywordMapping.put("힙한", "분위기");
        keywordMapping.put("모던", "분위기");
        keywordMapping.put("클래식", "분위기");
        keywordMapping.put("인생샷", "분위기");
        keywordMapping.put("인테리어", "분위기");
        keywordMapping.put("술집", "포차");
        keywordMapping.put("소주", "포차");
        keywordMapping.put("포차", "포차");
        keywordMapping.put("포장마차", "포차");

        keywordMapping.put("칵테일바", "칵테일");
        keywordMapping.put("와인바", "와인");
        keywordMapping.put("데이트", "데이트");
        
        keywordMapping.put("회식", "회식");
        keywordMapping.put("가족", "친구모임");
        keywordMapping.put("설렘/기대", "설렘/기대");
        keywordMapping.put("고독/외로움", "고독/감성적");
        //1-1 midtag에서 찾을 수 없는 데이터는 alltag에서 가져오도록 만들 키워드 작성
        keywordMapping.put("소고기", "기념/로맨틱");
        keywordMapping.put("생일", "기념/로맨틱");
        keywordMapping.put("파티", "기념/로맨틱");
        keywordMapping.put("컨셉", "기념/로맨틱");
        keywordMapping.put("친목모임", "친구모임");
        keywordMapping.put("데이트코스", "데이트/썸");
        keywordMapping.put("연인", "데이트/썸");
        keywordMapping.put("회식장소", "회식");
        keywordMapping.put("빈티지분위기", "분위기");
        keywordMapping.put("혼자", "슬픔");
        keywordMapping.put("기아", "스크린");
        keywordMapping.put("경기", "스크린");
        keywordMapping.put("야구", "스크린");
        keywordMapping.put("", "스트레스");
        keywordMapping.put("LP바", "혼술");
        keywordMapping.put("조용한", "혼술");
        keywordMapping.put("잔잔한", "혼술");
        keywordMapping.put("고요한", "혼술");
        keywordMapping.put("새벽", "혼술");
        keywordMapping.put("위로", "혼술");
     // --- '스트레스 해소' (칵테일, 분위기, 포차) 관련 키워드 ---
        keywordMapping.put("스트레스", "칵테일");
        keywordMapping.put("해소", "칵테일");
        keywordMapping.put("시끄러운", "포차");
        keywordMapping.put("신나는", "포차");
        keywordMapping.put("활기찬", "포차");
        
        keywordMapping.put("소개팅", "데이트");
        keywordMapping.put("커플", "데이트");
        
        
        // ★★★ 우리 공식 태그 이름 자체도 매핑해주는 것이 안전합니다. ★★★
        keywordMapping.put("스크린", "스크린");
        keywordMapping.put("한식", "한식");
        keywordMapping.put("일식", "일식");
        keywordMapping.put("디저트", "디저트");
        keywordMapping.put("막걸리", "막걸리");
        keywordMapping.put("가벼운", "가벼운");
        keywordMapping.put("혼술", "혼술");
        keywordMapping.put("맥주", "맥주");
        keywordMapping.put("회식", "회식");
        keywordMapping.put("친구", "친구");
        keywordMapping.put("분위기", "분위기");
        keywordMapping.put("포차", "포차");
        keywordMapping.put("와인", "와인");
        keywordMapping.put("데이트", "데이트");
        keywordMapping.put("위스키", "위스키");
        keywordMapping.put("칵테일", "칵테일");
        keywordMapping.put("케이크", "케이크");

        
        
        // (필요하다면 여기에 다른 매핑 규칙을 추가하세요)

        // 2. 모든 '현대어' 태그 정보를 미리 가져와 Map으로 만듭니다.
        Map<String, Long> tagNameToIdMap = moodTagRepository.findAll().stream()
                .collect(Collectors.toMap(MoodTagEntity::getTagName, MoodTagEntity::getTagId));
        
        System.out.println(">>>>> 전체 태그 수: " + tagNameToIdMap.size());

        // 3. 모든 가게 정보를 가져옵니다.
        List<StoreEntity> allStores = storeRepository.findAll();

        // 4. 기존 store_tag 데이터를 모두 삭제하여, 중복 실행을 방지합니다.
        storeTagRepository.deleteAllInBatch();
        System.out.println(">>>>> 기존 store_tag 데이터를 삭제했습니다.");

        int createdCount = 0;
        // 5. 각 가게를 순회하면서, 'mid_tag'의 내용을 분석하고 매핑합니다.
        for (StoreEntity store : allStores) {
            // ★★★ (핵심!) mid_tag가 없으면 all_keywords를 시도합니다. ★★★
            String rawKeywordsString = store.getMidTag(); // 우선 mid_tag를 시도
            if (rawKeywordsString == null || rawKeywordsString.trim().isEmpty()) {
                rawKeywordsString = store.getAllKeywords(); // mid_tag가 비어있으면 all_keywords를 사용
                if (rawKeywordsString == null || rawKeywordsString.trim().isEmpty()) continue; // 둘 다 없으면 건너뜀
            }

            String[] rawKeywords = rawKeywordsString.split(" ");

            Set<String> uniqueTagsForStore = new HashSet<>(); // 중복 태그 방지를 위한 Set
            for (String rawKeyword : rawKeywords) {
                String officialTagName = keywordMapping.getOrDefault(rawKeyword.trim(), rawKeyword.trim());

                Long tagId = tagNameToIdMap.get(officialTagName);
                
                if (tagId != null && !uniqueTagsForStore.contains(officialTagName)) { // 중복 태그 방지
                    StoreTagEntity newStoreTag = new StoreTagEntity();
                    newStoreTag.setStoreId(store.getStoreId());
                    newStoreTag.setTagId(tagId);
                    storeTagRepository.save(newStoreTag);
                    createdCount++;
                    uniqueTagsForStore.add(officialTagName); // 추가된 태그 기록
                }
            }
        }
        
        System.out.println(">>>>> 총 " + createdCount + "개의 가게-태그 관계가 생성되었습니다.");
        System.out.println(">>>>> 데이터 대통합 마이그레이션 완료!");
    }
    
    
    

    // (참고) 이전에 만들었던 다른 마이그레이션 메소드가 있다면, 그대로 두시면 됩니다.
    public void migrateLegalDongData() {
        // ... (이전 법정동 마이그레이션 로직) ...
        System.out.println("법정동 마이그레이션은 현재 호출되지 않았습니다.");
    }
}