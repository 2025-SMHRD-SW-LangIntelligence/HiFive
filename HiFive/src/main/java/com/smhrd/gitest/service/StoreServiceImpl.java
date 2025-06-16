package com.smhrd.gitest.service;

import org.springframework.stereotype.Service;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.repository.StoreRepository;

import java.util.List;
import java.util.Optional;


@Service //실제 구현될 서비스 내용
public class StoreServiceImpl implements StoreService {

	 private final StoreRepository storeRepository;

	    public StoreServiceImpl(StoreRepository storeRepository) {
	        this.storeRepository = storeRepository;
	    }

	    // 전체 술집 목록 조회
	    @Override
	    public List<StoreEntity> findAllStores() {
	        return storeRepository.findAll();
	    }

	    // 술집 ID로 조회
	    @Override
	    public Optional<StoreEntity> findStoreById(Long storeId) {
	        return storeRepository.findById(storeId);
	    }

	    // (예시) 상위 5개 술집만 가져오는 로직
	    @Override
	    public List<StoreEntity> getTopPicks() {
	        return storeRepository.findAll().stream().limit(5).toList();
	    }
	}