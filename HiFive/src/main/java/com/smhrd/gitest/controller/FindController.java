package com.smhrd.gitest.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smhrd.gitest.service.MemberService;

@Controller
public class FindController {
	@Autowired
	private MemberService memberService;
	
	  // 아이디 찾기 폼을 보여주는 페이지
    @GetMapping("/find/id")
    public String findIdForm() {
        return "find_id_form"; // templates/find_id_form.html
    }

    // 아이디 찾기 처리
    @PostMapping("/find/id")
    public String handleFindId(@RequestParam String nickname,
                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthdate,
                               Model model) {
        Optional<String> foundEmail = memberService.findEmailByNicknameAndBirthdate(nickname, birthdate);

        if (foundEmail.isPresent()) {
            model.addAttribute("foundEmail", foundEmail.get());
        } else {
            model.addAttribute("errorMessage", "일치하는 회원 정보가 없습니다.");
        }
        return "find_id_result"; // templates/find_id_result.html
    }

}
