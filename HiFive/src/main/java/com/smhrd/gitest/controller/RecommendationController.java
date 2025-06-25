package com.smhrd.gitest.controller;

import com.smhrd.gitest.dto.StoreResponseDto;
import com.smhrd.gitest.service.AddressMappingService;
import com.smhrd.gitest.service.StoreSearchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // ★★★ 1. RedirectAttributes import 추가

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecommendationController {

    @Autowired
    private AddressMappingService addressMappingService;

    @Autowired
    private StoreSearchService storeSearchService;

    // POST 요청을 처리하는 메소드 (사용자가 검색 버튼을 눌렀을 때)
    @PostMapping("/recommend")
    public String getRecommendations(
            // HTML의 name 속성에 맞춰 파라미터를 받습니다.
            @RequestParam(value = "situationTags", required = false) List<String> situationTags,
            @RequestParam(value = "emotionTags", required = false) List<String> emotionTags,
            @RequestParam(value = "addressTags", required = false) List<String> addressTags,
            HttpSession session,
            RedirectAttributes redirectAttributes, // ★★★ 2. 에러 메시지 전달을 위해 파라미터 추가
            Model model) {

        // ★★★ 3. (로직 시작) 비회원 태그 개수 검증 ★★★
        if (session.getAttribute("loginUser") == null) {
            if ((situationTags != null && situationTags.size() > 1) || (emotionTags != null && emotionTags.size() > 1)) {
                // 비회원이 태그를 2개 이상 선택했다면, 경고 메시지와 함께 메인 페이지로 돌려보냅니다.
                redirectAttributes.addFlashAttribute("error", "비회원은 각 태그를 1개씩만 선택할 수 있습니다.");
                return "redirect:/main";
            }
        }
        
        // ★★★ 4. 선택된 모든 태그들을 하나의 'categories' 리스트로 통합 ★★★
        List<String> categories = new ArrayList<>();
        if (situationTags != null) categories.addAll(situationTags);
        if (emotionTags != null) categories.addAll(emotionTags);

        // ★★★ 5. 새로운 서비스들을 사용하여 검색 로직 실행 (중복 코드 제거) ★★★
        List<String> legalDongsToSearch = addressMappingService.getLegalDongsForSearch(addressTags);
        List<StoreResponseDto> recommendedStores = storeSearchService.search(legalDongsToSearch, categories);

        // ★★★ 6. 다음 새로고침을 위해, 사용자의 선택을 세션에 저장 ★★★
        // 이제 통합된 categories를 세션에 저장합니다.
        session.setAttribute("lastCategories", categories);
        session.setAttribute("lastAddressTags", addressTags);

        // ★★★ 7. 결과를 모델에 담아 View(HTML)로 전달 ★★★
        model.addAttribute("recommendedStores", recommendedStores);
        model.addAttribute("selectedTags", categories.isEmpty() ? List.of("전체") : categories);

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

        // 세션의 조건으로도 똑같이 새로운 검색 로직을 실행합니다.
        List<String> legalDongsToSearch = addressMappingService.getLegalDongsForSearch(lastAddressTags);
        List<StoreResponseDto> recommendedStores = storeSearchService.search(legalDongsToSearch, lastCategories);

        // 모델에 데이터 담기
        model.addAttribute("recommendedStores", recommendedStores);
        model.addAttribute("selectedTags", lastCategories.isEmpty() ? List.of("전체") : lastCategories);

        return "recommend";
    }
}