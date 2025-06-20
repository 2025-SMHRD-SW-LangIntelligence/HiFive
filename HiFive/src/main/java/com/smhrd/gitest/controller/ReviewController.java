package com.smhrd.gitest.controller;

import com.smhrd.gitest.entity.MemberEntity;
import com.smhrd.gitest.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews/add")
    public String addReview(@RequestParam("storeId") Long storeId, // ★★★ 여기서 'storeId'가 선언되었습니다.
                            @RequestParam("content") String content,
                            HttpSession session) {
        
        // 1. 세션에서 로그인한 사용자 정보 가져오기
        MemberEntity loginUser = (MemberEntity) session.getAttribute("loginUser");

        // 2. 비로그인 사용자는 로그인 페이지로 리다이렉트
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 3. 서비스 호출하여 리뷰 저장
        reviewService.saveReview(loginUser, storeId, content);

        // 4. 리뷰를 작성한 가게의 상세 페이지로 다시 돌아가기
        // ★★★ 'storeId'는 이 메소드 안에서만 유효합니다. ★★★
        return "redirect:/store/" + storeId; 
    }
    
    // (만약 다른 메소드에서 에러가 났다면, 그 메소드에는 storeId 변수가 없기 때문입니다.)
}