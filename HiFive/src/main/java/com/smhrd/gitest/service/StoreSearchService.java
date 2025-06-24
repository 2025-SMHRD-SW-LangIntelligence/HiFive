package com.smhrd.gitest.service;

import com.smhrd.gitest.dto.StoreResponseDto;
import java.util.List;

/**
 * 가게 검색 로직을 처리하는 서비스의 역할을 정의하는 인터페이스입니다.
 */
public interface StoreSearchService {

    /**
     * 법정동 리스트와 분위기 태그 리스트를 받아 조건에 맞는 가게 목록을 반환합니다.
     * @param legalDongs 검색할 법정동 목록 (예: ["산수1동", "산수2동"])
     * @param moodTags 검색할 분위기 태그 목록 (예: ["데이트", "분위기"])
     * @return 검색 조건에 맞는 가게 정보 DTO 리스트
     */
    List<StoreResponseDto> search(List<String> legalDongs, List<String> moodTags);

}