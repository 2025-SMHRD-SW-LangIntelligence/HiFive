package com.smhrd.gitest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smhrd.gitest.Entity.MemberEntity;
import com.smhrd.gitest.memberService.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
		
	@Autowired
	MemberService memberService;
	// 회원가입 기능
		@PostMapping("/register.do")
		public String register(@RequestParam String email, @RequestParam String pw, 
				@RequestParam String nickname, @RequestParam int age ,@RequestParam String sex) {
			//1. 필요한거 ???
			// --> id, pw, age, name
			//2. DB 연결  --> Repository 연결, Entity 생성 --> Service
			//3. Service 연결
			MemberEntity entity = new MemberEntity();
			entity.setEmail(email);
			entity.setPw(pw);
			entity.setNickname(nickname);
			entity.setAge(age);
			entity.setSex(sex);
			
			String result =memberService.register(entity);
			
			if(result.equals("success")){
				return "redirect:/login";
			}else {
				return "redirect:/register";
			}
								
					
			
			
			
			
		}
		
		// 로그인 기능
		@PostMapping("login.do")
		public String login(@RequestParam String email,@RequestParam String pw ,HttpSession session){
			//1. 필요한 거
			// id,pw
			//2. DB연결 --> service --> repository 연결 --> repository 적절한 메소드 생성(사용)
			
			//찐 2번 service 연결
			MemberEntity user = memberService.login(email, pw);
			if(user != null) {//로그인 성공
				session.setAttribute("user", user);
				return "redirect:/";
			}else {
				return "redirect:/login";
			}
			//로그인이 성공하면 로그인 정보를 저장 후 index 페이지로 이동 session
			
		}
		// 로그아웃 기능
		@GetMapping("/logout")
		public String logout(HttpSession session) {
			session.removeAttribute("user");
			return "redirect:/";
		}
		
		

} 
