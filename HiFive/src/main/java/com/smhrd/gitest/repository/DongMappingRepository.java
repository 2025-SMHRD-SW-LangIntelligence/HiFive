package com.smhrd.gitest.repository;
//법정동을 행정동으로 검색되도록 정의하는 곳

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.gitest.entity.DongMappingEntity;
@Repository
public interface DongMappingRepository extends JpaRepository<DongMappingEntity, Long>{
	 // 여러 행정동을 한 번에 조회하여 법정동 리스트를 가져오는 메서드
    List<DongMappingEntity> findByAdminDongIn(List<String> adminDongs);
}
