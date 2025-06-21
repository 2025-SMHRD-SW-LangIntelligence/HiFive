// main.js

// --- 데이터 정의 ---
const dongData = {
    "동구": ["충장동", "동명동", "계림1동", "계림2동", "산수1동", "산수2동", "지산1동", "지산2동", "서남동", "학동", "학운동", "지원1동", "지원2동"],
    "서구": ["서창동", "금호1동", "금호2동", "풍암동", "농성1동", "농성2동", "치평동", "유덕동", "동천동", "광천동", "양동", "양3동", "화정1동", "화정2동", "화정3동", "화정4동", "상무1동", "상무2동"],
    "남구": ["양림동", "방림1동", "방림2동", "봉선1동", "봉선2동", "사직동", "월산동", "월산4동", "월산5동", "백운1동", "백운2동", "주월1동", "주월2동", "진월동", "효덕동", "송암동", "대촌동"],
    "북구": ["중흥동", "중흥1동", "중앙동", "임동", "신안동", "용봉동", "운암1동", "운암2동", "운암3동", "동림동", "우산동", "풍향향동", "문화동", "문흥1동", "문흥2동", "두암1동", "두암2동", "두암3동", "삼각동", "일곡동", "매곡동", "오치1동", "오치2동", "석곡동", "건국동", "양산동", "신용동"],
    "광산구": ["송정1동", "송정2동", "도산동", "신흥동", "어룡동", "우산동", "월곡1동", "월곡2동", "운남동", "하남동", "첨단1동", "첨단2동", "비아동", "수완동", "신가동", "신창동", "평동", "임곡동", "동곡동", "삼도동", "본량동"]
};

// --- HTML 요소 선택 ---
const districtCheckboxes = document.querySelectorAll("#districts .tag-checkbox");
const neighborhoodsContainer = document.getElementById("neighborhoods");
const gwangjuCityTag = document.getElementById('gwangju-city-tag');

// --- 이벤트 리스너 추가 ---

// 1. "광주광역시" 태그 클릭 이벤트
if (gwangjuCityTag) {
    gwangjuCityTag.addEventListener('click', function() {
        // 자기 자신의 'selected' 클래스만 제어합니다.
        this.classList.toggle('selected');
    });
}

// 2. "구" 체크박스 변경 이벤트
districtCheckboxes.forEach((checkbox) => {
    checkbox.addEventListener('change', () => {
        if (checkbox.checked) {
            // 다른 '구' 체크박스는 선택 해제
            districtCheckboxes.forEach(cb => {
                if (cb !== checkbox) cb.checked = false;
            });

            const dongKey = checkbox.value;
            const dongs = dongData[dongKey] || [];

            neighborhoodsContainer.innerHTML = dongs
                .map((dong, index) => {
                    const dongId = `dong-${dongKey}-${index}`;
                    return `
                        <input type="checkbox" name="addressTags" value="${dong}" id="${dongId}" class="tag-checkbox">
                        <label for="${dongId}" class="tag">${dong}</label>
                    `;
                })
                .join("");
        } else {
            neighborhoodsContainer.innerHTML = "";
        }
    });
});