package com.smhrd.gitest.controller;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.entity.MemberEntity;
import com.smhrd.gitest.service.MemberWishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberWishlistController {

    @Autowired
    private MemberWishlistService wishlistService;

    // 찜 추가
    @PostMapping("/wishlist/add")
    public String addWishlist(@RequestParam Long storeId, HttpSession session) {
        MemberEntity user = (MemberEntity) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }
        wishlistService.addWishlist(user.getEmail(), storeId);
        return "redirect:/store/" + storeId; // 원하는 페이지로 리다이렉트
    }

    // 찜 취소
    @PostMapping("/wishlist/remove")
    public String removeWishlist(@RequestParam Long storeId, HttpSession session) {
        MemberEntity user = (MemberEntity) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }
        wishlistService.removeWishlist(user.getEmail(), storeId);
        return "redirect:/store/" + storeId;
    }

    // 내 찜 목록 보기 (마이페이지 등)
    @GetMapping("/wishlist/mine")
    public String myWishlist(HttpSession session, Model model) {
        MemberEntity user = (MemberEntity) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("wishList", wishlistService.getMyWishlist(user.getEmail()));
        return "mywishlist"; // templates/mywishlist.html로 이동
    }
   
}