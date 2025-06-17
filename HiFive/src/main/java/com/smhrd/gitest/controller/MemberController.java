package com.smhrd.gitest.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smhrd.gitest.entity.MemberEntity;
import com.smhrd.gitest.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

    
		
	@Autowired
	MemberService memberService;

   
    
	// 회원가입 기능
		@PostMapping("/register")
		public String register(@RequestParam String email, @RequestParam String pw, 
				@RequestParam String nickname, @RequestParam int age ,@RequestParam String gender) {
			//1. 필요한거 ???
			// --> id, pw, age, name
			//2. DB 연결  --> Repository 연결, Entity 생성 --> Service
			//3. Service 연결
			MemberEntity entity = new MemberEntity();
			entity.setEmail(email);
			entity.setPw(pw);
			entity.setNickname(nickname);
			entity.setAge(age);
			entity.setGender(gender);
			
			 String result = memberService.register(entity);
			 
			 if ("success".equals(result)) {
			        return "redirect:/login";
			    } else if ("duplicate".equals(result)) {
			        return "redirect:/register?error=duplicate";
			    }
			    // 모든 경우를 대비한 디폴트 반환
			    return "redirect:/register?error=fail";
		}
			
			//로그인 로그아웃 구현
			@PostMapping("/login")
			public String login(@RequestParam String email, @RequestParam String pw, HttpSession session) {
			    MemberEntity user = memberService.login(email, pw);
			    if (user != null) {
			        session.setAttribute("loginUser", user);
			        return "redirect:/"; // 로그인 성공시 메인으로
			    } else {
			        return "redirect:/login?error";
			    }
			}

			@GetMapping("/logout")
			public String logout(HttpSession session) {
			    session.invalidate();
			    return "redirect:/login?logout";
			
			
//			if(result.equals("success")){
//				return "redirect:/login";
//			}else {
//				return "redirect:/register";
//			}
								
					
			
			
			
			
		}
		
		
		
		
		
		

} 
