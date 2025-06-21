package com.smhrd.gitest.controller;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.service.StoreService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecommendationController {

	@Autowired
	private StoreService storeService;

	@PostMapping("/recommend")
	public String getRecommendations(
			@RequestParam(value = "categories", required = false) List<String> categories,
			@RequestParam(value = "addressTags", required = false) List<String> addressTags,
			HttpSession session,  // HttpSession 파라미터 추가
			Model model) {

		List<StoreDto> recommendedStores = storeService.recommendByFilters(categories, addressTags);
		
		session.setAttribute("lastCategories", categories);
		session.setAttribute("lastAddressTags", addressTags);
		
		
		
		
		
		// 선택된 태그들을 모델에 담아 결과 페이지에 표시
		List<String> selectedTags = new ArrayList<>();
		if (categories != null)
			selectedTags.addAll(categories);
		if (addressTags != null)
			selectedTags.addAll(addressTags);

		model.addAttribute("recommendedStores", recommendedStores);
		model.addAttribute("selectedTags", selectedTags.isEmpty() ? List.of("전체") : selectedTags);

		return "recommend";
	}
	
	 @GetMapping("/recommend")
	    public String showLastRecommendation(HttpSession session, Model model) {
	        // ★★★ 세션에서 마지막 검색 조건들을 가져옴 ★★★
	        List<String> lastCategories = (List<String>) session.getAttribute("lastCategories");
	        List<String> lastAddressTags = (List<String>) session.getAttribute("lastAddressTags");

	        // 만약 세션에 검색 기록이 없으면 (예: 직접 URL로 접근), 메인 페이지로 보냄
	        if (lastCategories == null && lastAddressTags == null) {
	            return "redirect:/main";
	        }

	        // 세션의 조건으로 추천 로직 다시 실행
	        List<StoreDto> recommendedStores = storeService.recommendByFilters(lastCategories, lastAddressTags);

	        // 모델에 데이터 담기
	        List<String> selectedTags = new ArrayList<>();
	        if (lastCategories != null) selectedTags.addAll(lastCategories);
	        if (lastAddressTags != null) selectedTags.addAll(lastAddressTags);
	        
	        model.addAttribute("recommendedStores", recommendedStores);
	        model.addAttribute("selectedTags", selectedTags.isEmpty() ? List.of("전체") : selectedTags);

	        return "recommend";
	
	 }
}