package com.smhrd.gitest.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// 프론트에서 입력값이 json으로 넘어오는데 그 값을 보여주는...
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto {	

    /**
     * 사용자가 선택한 동네(행정동) 이름 리스트입니다.
     * 프론트엔드 JSON 예시: { "addressTags": ["산수동", "양림동"] }
     */
	  private List<String> addressTags;
	  /**
	   * 사용자가 선택한 감정/분위기 태그 리스트입니다.
	   * 프론트엔드 JSON 예시: { "moodTags": ["데이트", "분위기"] }
	   */
	  private List<String> moodTags;

}
