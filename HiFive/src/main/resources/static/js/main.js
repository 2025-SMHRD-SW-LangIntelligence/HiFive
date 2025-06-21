// "구" 선택과 관련된 label.tag 요소들을 모두 선택합니다.
const districtLabels = document.querySelectorAll("#districts label.tag");
const neighborhoodsContainer = document.getElementById("neighborhoods");

// "구" 데이터
const dongData = {
    donggu: ["충장동", "동명동", "계림1동", "계림2동", "산수1동", "산수2동", "지산1동", "지산2동", "서남동", "학동", "학운동", "지원1동", "지원2동"],
    seogu: ["서창동", "금호1동", "금호2동", "풍암동", "농성1동", "농성2동", "치평동", "유덕동", "동천동", "광천동", "양동", "양3동", "화정1동", "화정2동", "화정3동", "화정4동", "상무1동", "상무2동"],
    namgu: ["양림동", "방림1동", "방림2동", "봉선1동", "봉선2동", "사직동", "월산동", "월산4동", "월산5동", "백운1동", "백운2동", "주월1동", "주월2동", "진월동", "효덕동", "송암동", "대촌동"],
    bukgu: ["중흥동", "중흥1동", "중앙동", "임동", "신안동", "용봉동", "운암1동", "운암2동", "운암3동", "동림동", "우산동", "풍향동", "문화동", "문흥1동", "문흥2동", "두암1동", "두암2동", "두암3동", "삼각동", "일곡동", "매곡동", "오치1동", "오치2동", "석곡동", "건국동", "양산동", "신용동"],
    gwangsan: ["송정1동", "송정2동", "도산동", "신흥동", "어룡동", "우산동", "월곡1동", "월곡2동", "운남동", "하남동", "첨단1동", "첨단2동", "비아동", "수완동", "신가동", "신창동", "평동", "임곡동", "동곡동", "삼도동", "본량동"]
};

// 각 "구" 라벨에 클릭 이벤트를 추가합니다.
districtLabels.forEach((label) => {
    label.addEventListener("click", () => {
        // 모든 '구' 라벨의 selected 클래스를 먼저 제거합니다.
        districtLabels.forEach(l => l.classList.remove("selected"));
        // 현재 클릭한 라벨에만 selected 클래스를 추가합니다.
        label.classList.add("selected");

        // 연결된 체크박스에서 value 값을 가져옵니다. (예: '동구')
        const checkbox = document.getElementById(label.getAttribute('for'));
        const dongKey = checkbox.value.replace('구', '').toLowerCase(); // '동구' -> 'donggu'
        const dongs = dongData[dongKey] || [];

        // ★★★ "동" 목록을 input-label 쌍으로 생성합니다. ★★★
        neighborhoodsContainer.innerHTML = dongs
            .map((dong, index) => {
                const dongId = `dong-${dongKey}-${index}`;
                return `
                    <input type="checkbox" name="addressTags" value="${dong}" id="${dongId}" class="tag-checkbox">
                    <label for="${dongId}" class="tag">${dong}</label>
                `;
            })
            .join("");

        // 새로 생성된 "동" 태그들에도 선택 효과를 위한 이벤트 리스너를 추가합니다.
        document.querySelectorAll("#neighborhoods label.tag").forEach((t) => {
            t.addEventListener("click", () => t.classList.toggle("selected"));
        });
    });
});