package com.smhrd.gitest.controller;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecommendationController {

	@Autowired
	private StoreService storeService;

	@PostMapping("/recommend")
	public String getRecommendations(@RequestParam(value = "categories", required = false) List<String> categories,
			@RequestParam(// ★★★ 주소 태그 파라미터 추가 ★★★
					value = "addressTags", required = false) List<String> addressTags, 
			Model model) {

		List<StoreDto> recommendedStores= storeService.recommendByFilters(categories,addressTags);

		 // 선택된 태그들을 모델에 담아 결과 페이지에 표시
	    List<String> selectedTags = new ArrayList<>();
	    if (categories != null) selectedTags.addAll(categories);
	    if (addressTags != null) selectedTags.addAll(addressTags);

	    model.addAttribute("recommendedStores", recommendedStores);
	    model.addAttribute("selectedTags", selectedTags.isEmpty() ? List.of("전체") : selectedTags);

	    return "recommend";
	}
	
}