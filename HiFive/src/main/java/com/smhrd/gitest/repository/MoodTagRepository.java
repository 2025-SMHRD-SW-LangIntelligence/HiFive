package com.smhrd.gitest.repository;

import com.smhrd.gitest.entity.MoodTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoodTagRepository extends JpaRepository<MoodTagEntity, Long> {

    // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
    // ★★★ (최종 해결책!) JPA가 메소드 이름을 해석하지 못하도록, @Query로 직접 명령합니다. ★★★
    // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
    /**
     * 태그 이름(tagName) 리스트를 받아, 해당하는 모든 태그의 ID(tagId)를 조회합니다.
     * @param tagNames 검색할 태그 이름들의 리스트
     * @return 태그 ID들의 리스트
     */
    @Query("SELECT m.tagId FROM MoodTagEntity m WHERE m.tagName IN :tagNames")
    List<Long> findTagIdsByTagNames(@Param("tagNames") List<String> tagNames);

}