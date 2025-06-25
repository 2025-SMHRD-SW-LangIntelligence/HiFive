package com.smhrd.gitest.controller;

import com.smhrd.gitest.dto.StoreDto;
import com.smhrd.gitest.entity.MemberEntity;
import com.smhrd.gitest.entity.MemberWishlistEntity;
import com.smhrd.gitest.entity.StoreEntity;
import com.smhrd.gitest.service.MemberWishlistService;
import com.smhrd.gitest.service.StoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MemberWishlistController {

    @Autowired
    private MemberWishlistService wishlistService;
    
    @Autowired
    private StoreService storeService;

    // --- 찜 추가 기능 (기존과 동일, 페이지 이동 방식) ---
    @PostMapping("/wishlist/add")
    public String addWishlist(@RequestParam Long storeId, HttpSession session) {
        MemberEntity user = (MemberEntity) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }
        wishlistService.addWishlist(user.getEmail(), storeId);
        return "redirect:/store/" + storeId;
    }
    
    // --- (수정됨) 찜 취소 기능 ---
    // 기존의 /wishlist/remove는 더 이상 사용하지 않습니다.
    // 대신, JavaScript(AJAX) 전용 API를 새로 만듭니다.
    
    /**
     * (신규) JavaScript의 fetch 요청을 받아 찜을 취소하는 API입니다.
     * 페이지를 이동시키지 않고, 성공/실패 상태만 응답합니다.
     * @param storeId 찜을 취소할 가게의 ID
     * @param session 현재 로그인한 사용자 정보
     * @return 성공 시 200 OK, 실패 시 500 Internal Server Error
     */
    @DeleteMapping("/api/wishlist/remove") // ★★★ 1. RESTful한 새 주소와 @DeleteMapping 사용
    @ResponseBody // ★★★ 2. 페이지가 아닌 데이터(JSON/text)를 응답하기 위한 어노테이션
    public ResponseEntity<?> removeWishlistApi(@RequestParam Long storeId, HttpSession session) {
        MemberEntity user = (MemberEntity) session.getAttribute("loginUser");
        if (user == null) {
            // 로그인되지 않은 사용자의 요청은 '권한 없음(401)'으로 응답합니다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        try {
            wishlistService.removeWishlist(user.getEmail(), storeId);
            // 성공 시, "성공"이라는 텍스트와 함께 200 OK 상태를 응답합니다.
            return ResponseEntity.ok("찜이 취소되었습니다.");
        } catch (Exception e) {
            System.err.println("찜 취소 API 처리 중 에러: " + e.getMessage());
            // 실패 시, 에러 메시지와 함께 500 상태를 응답합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찜 취소 중 오류가 발생했습니다.");
        }
    }


    // --- 내 찜 목록 보기 (수정 필요 없음) ---
    @GetMapping("/wishlist/mine")
    public String myWishlist(HttpSession session, Model model) {
        MemberEntity user = (MemberEntity) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<MemberWishlistEntity> myWishes = wishlistService.getMyWishlist(user.getEmail());
        
        // ★★★ 이 로직은 매우 중요합니다. ★★★
        // StoreDto에 'wishlistId'도 함께 담아주어야, 프론트에서 어떤 찜을 취소할지 알 수 있습니다.
        List<StoreDto> wishlistedStores = myWishes.stream()
            .map(wish -> {
            	// 가게 ID로 가게 정보를 찾습니다.
                Optional<StoreEntity> storeOpt = storeService.findStoreById(wish.getStoreId());
                // 가게 정보가 존재할 경우에만 DTO를 생성합니다.
                if (storeOpt.isPresent()) {
                    StoreEntity storeEntity = storeOpt.get();
                    StoreDto dto = StoreDto.fromEntity(storeEntity);
                    
                    dto.setWishlistId(wish.getIdx()); // MemberWishlistEntity의 ID를 가져옵니다.
                    // dto.setWishlistId(wish.getId()); // StoreDto에 wishlistId 필드와 setter 추가 필요
                    return dto;
                }
                // 가계정보 없을시 'null'을 가져옵니다.
                return null;
            })
            .filter(Objects::nonNull)  // ★★★ (핵심!) 리스트에서 모든 null 값을 완벽하게 제거합니다.
            .collect(Collectors.toList());
        // 3. 모델에 최종적으로 필터링된, 안전한 리스트를 담아 전달
        model.addAttribute("wishlistedStores", wishlistedStores);
        
        return "mywishlist"; // 또는 mypage.html 등 찜 목록을 보여주는 페이지
    }
}