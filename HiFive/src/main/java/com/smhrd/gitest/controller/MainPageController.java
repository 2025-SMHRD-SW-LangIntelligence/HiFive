package com.smhrd.gitest.controller;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.entity.MemberEntity;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.service.MemberWishlistService;
import com.smhrd.gitest.service.StoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainPageController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private MemberWishlistService wishlistService;

    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        // 1. 로그인 사용자 정보 가져오기 (없으면 null)
        MemberEntity user = (MemberEntity) session.getAttribute("loginUser");

        // 2. 추천 술집 리스트(엔티티) 가져오기
        List<StoreEntity> entityList = storeService.getTopPicks();

        // 3. 엔티티 → DTO 변환
        List<StoreDto> topPicks = entityList.stream()
            .map(StoreDto::fromEntity)
            .toList();

        // 4. 로그인 상태에 따라 찜 여부(wishlisted) 세팅
        if (user != null) {
            // 로그인 상태면 DB에서 찜 여부 체크
            for (StoreDto store : topPicks) {
                boolean wishlisted = wishlistService.existsByEmailAndStoreId(user.getEmail(), store.getStoreId());
                store.setWishlisted(wishlisted);
            }
        } else {
            // 비로그인 상태면 모두 false로 세팅
            for (StoreDto store : topPicks) {
                store.setWishlisted(false);
            }
        }

        // 5. 모델에 topPicks 리스트 담기
        model.addAttribute("topPicks", topPicks);

        return "main"; // templates/main.html 랜더링
    }
}