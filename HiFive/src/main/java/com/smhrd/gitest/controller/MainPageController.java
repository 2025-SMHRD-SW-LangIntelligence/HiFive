package com.smhrd.gitest.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.entity.MemberEntity;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.service.MemberWishlistService;
import com.smhrd.gitest.service.StoreService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainPageController {
	
	@Autowired
    private StoreService storeService;
	
	@Autowired
	 private MemberWishlistService wishlistService; 
	
   
    // 시작할때 메인페이지로 안내
	@GetMapping("/")
	public String mainPage(HttpSession session, Model model) {
	    MemberEntity user = (MemberEntity) session.getAttribute("loginUser");
	    List<StoreEntity> entityList = storeService.getTopPicks();

	    // 엔티티 → DTO 변환
	    List<StoreDto> topPicks = entityList.stream()
	        .map(StoreDto::fromEntity)
	        .toList();

	    for (StoreDto store : topPicks) {
	        boolean wishlisted = (user != null)
	            && wishlistService.existsByEmailAndStoreId(user.getEmail(), store.getStoreId());
	        store.setWishlisted(wishlisted);
	    }

	    model.addAttribute("topPicks", topPicks);
	    return "main";
	}
        
    }
   



