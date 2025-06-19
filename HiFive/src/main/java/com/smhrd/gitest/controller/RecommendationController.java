package com.smhrd.gitest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.service.StoreService;

@Controller
public class RecommendationController {
	@Autowired
	private StoreService storeService;
	
	 @PostMapping("/recommend")
	    public String getRecommendations(@RequestParam("tags") List<String> tags, Model model) {
	        // 서비스 호출하여 추천 술집 리스트 받기
	        List<StoreDto> recommendedStores = storeService.recommendStoresByTags(tags);

	        // 모델에 추천 결과 담기
	        model.addAttribute("recommendedStores", recommendedStores);
	        model.addAttribute("selectedTags", tags);

	        // ★★★ 추천 결과를 보여줄 페이지로 이동 ★★★
	        return "result"; // templates/result.html
	    }
}
