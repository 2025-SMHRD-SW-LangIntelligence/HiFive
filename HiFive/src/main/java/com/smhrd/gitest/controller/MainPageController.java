package com.smhrd.gitest.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.smhrd.gitest.service.StoreService;

@Controller
public class MainPageController {

    private final StoreService storeService;

    public MainPageController(StoreService storeService) {
        this.storeService = storeService;
    }
    // 시작할때 메인페이지로 안내
    @GetMapping("/")
    public String showMainPage(Model model) {
        model.addAttribute("topPicks", storeService.getTopPicks());
        return "main";
    }
}