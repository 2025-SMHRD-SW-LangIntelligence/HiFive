package com.smhrd.gitest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {

    private Long storeId;
    private String shopName;
    private String cleanAddress;
    private String photoUrl;
    private Double rating;
    private List<String> matchedTags;

    // ★★★ 문제를 일으키던 fromEntity 메소드를 완전히 삭제하여,
    //     미래에 또 같은 실수가 발생하는 것을 원천 차단합니다. ★★★
}