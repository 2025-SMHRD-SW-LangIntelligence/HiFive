package com.smhrd.gitest.controller;

import com.smhrd.gitest.dto.StoreResponseDto; // StoreDto 대신 StoreResponseDto를 사용합니다.
import com.smhrd.gitest.service.AddressMappingService; // 새로운 서비스 import
import com.smhrd.gitest.service.StoreSearchService; // 새로운 서비스 import
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

    // 1. 기존 StoreService 대신, 새로운 서비스들을 주입받습니다.
    @Autowired
    private AddressMappingService addressMappingService;

    @Autowired
    private StoreSearchService storeSearchService;

    // POST 요청을 처리하는 메소드 (사용자가 검색 버튼을 눌렀을 때)
    @PostMapping("/recommend")
    public String getRecommendations(
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "addressTags", required = false) List<String> addressTags,
            HttpSession session,
            Model model) {

        // 2. (핵심!) 새로운 서비스들을 사용하여 검색 로직을 실행합니다.
        // 2-1. 행정동 -> 법정동으로 변환
        List<String> legalDongsToSearch = addressMappingService.getLegalDongsForSearch(addressTags);
        // 2-2. 변환된 법정동과 분위기 태그로 가게 검색
        List<StoreResponseDto> recommendedStores = storeSearchService.search(legalDongsToSearch, categories);

        // 3. 다음 새로고침을 위해, 사용자의 선택을 세션에 저장합니다.
        session.setAttribute("lastCategories", categories);
        session.setAttribute("lastAddressTags", addressTags);

        // 4. 결과를 모델에 담아 View(HTML)로 전달합니다.
        List<String> selectedTags = new ArrayList<>();
        if (categories != null) selectedTags.addAll(categories);
        if (addressTags != null) selectedTags.addAll(addressTags);

        model.addAttribute("recommendedStores", recommendedStores);
        model.addAttribute("selectedTags", selectedTags.isEmpty() ? List.of("전체") : selectedTags);

        return "recommend"; // recommend.html 페이지를 보여줍니다.
    }

    // GET 요청을 처리하는 메소드 (페이지를 새로고침했거나, URL로 직접 접근했을 때)
    @GetMapping("/recommend")
    public String showLastRecommendation(HttpSession session, Model model) {
        // 세션에서 마지막 검색 조건들을 가져옵니다.
        List<String> lastCategories = (List<String>) session.getAttribute("lastCategories");
        List<String> lastAddressTags = (List<String>) session.getAttribute("lastAddressTags");

        if (lastCategories == null && lastAddressTags == null) {
            return "redirect:/main";
        }

        // (핵심!) 세션의 조건으로도 똑같이 새로운 검색 로직을 실행합니다.
        List<String> legalDongsToSearch = addressMappingService.getLegalDongsForSearch(lastAddressTags);
        List<StoreResponseDto> recommendedStores = storeSearchService.search(legalDongsToSearch, lastCategories);

        // 모델에 데이터 담기
        List<String> selectedTags = new ArrayList<>();
        if (lastCategories != null) selectedTags.addAll(lastCategories);
        if (lastAddressTags != null) selectedTags.addAll(lastAddressTags);
        
        model.addAttribute("recommendedStores", recommendedStores);
        model.addAttribute("selectedTags", selectedTags.isEmpty() ? List.of("전체") : selectedTags);

        return "recommend";
    }
}