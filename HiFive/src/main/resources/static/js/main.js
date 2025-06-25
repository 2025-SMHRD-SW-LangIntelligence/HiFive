// main.js

// --- 데이터 정의 ---
const dongData = {
	"동구": ["충장동", "동명동", "계림동", "산수동", "지산동", "서남동", "학동", "학운동", "지원동"],
	"서구": ["서창동", "금호동", "풍암동", "농성동", "치평동", "유덕동", "동천동", "광천동", "양동", "화정동", "상무동"],
	"남구": ["양림동", "방림동", "봉선동", "사직동", "월산동", "백운동", "주월동", "진월동", "효덕동", "송암동", "대촌동"],
	"북구": ["중흥동", "중앙동", "임동", "신안동", "용봉동", "운암동", "동림동", "우산동", "풍향동", "문화동", "문흥동", "두암동", "삼각동", "일곡동", "매곡동", "오치동", "석곡동", "건국동", "양산동", "신용동"],
	"광산구": ["송정동", "도산동", "신흥동", "어룡동", "우산동", "월곡동", "운남동", "하남동", "첨단동", "비아동", "수완동", "신가동", "신창동", "평동", "임곡동", "동곡동", "삼도동", "본량동"]
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

// --- 2. "구" 체크박스 변경 이벤트 수정 ---
districtCheckboxes.forEach((checkbox) => {
	checkbox.addEventListener('change', () => {
		if (checkbox.checked) {
			// 다른 구 체크 해제
			districtCheckboxes.forEach(cb => {
				if (cb !== checkbox) cb.checked = false;
			});

			const dongKey = checkbox.value;
			const dongs = dongData[dongKey] || [];

			// "동" 체크박스 목록 갱신
			neighborhoodsContainer.innerHTML = dongs
				.map((dong, index) => {
					const dongId = `dong-${dongKey}-${index}`;
					return `
                        <input type="checkbox" name="addressTags" value="${dong}" id="${dongId}" class="tag-checkbox">
                        <label for="${dongId}" class="tag">${dong}</label>
                    `;
				})
				.join("");

			// ✅ "동" 체크박스 단일 선택 처리
			const dongCheckboxes = neighborhoodsContainer.querySelectorAll('input[type="checkbox"]');
			dongCheckboxes.forEach(cb => {
				cb.addEventListener('change', () => {
					dongCheckboxes.forEach(other => {
						if (other !== cb) other.checked = false;
					});
				});
			});

		} else {
			neighborhoodsContainer.innerHTML = "";
		}
	});
});

// --- 태그 선택 제한 로직 ---

// isGuest 변수는 main.html에서 이미 선언되었다고 가정합니다.
if (isGuest) {
	// 1. 상황 태그 그룹에 대한 제한 로직
	const situationTags = document.querySelectorAll('input[name="situationTags"]');
	situationTags.forEach(checkbox => {
		checkbox.addEventListener('click', (event) => {
			// 이미 선택된 개수를 센다.
			const checkedCount = document.querySelectorAll('input[name="situationTags"]:checked').length;

			// 1개를 초과하여 선택하려고 하면,
			if (checkedCount > 1) {
				alert('비회원은 상황 태그를 1개만 선택할 수 있습니다.');
				event.target.checked = false; // 선택을 취소시킨다.
			}
		});
	});

	// 2. 감정 태그 그룹에 대한 제한 로직
	const emotionTags = document.querySelectorAll('input[name="emotionTags"]');
	emotionTags.forEach(checkbox => {
		checkbox.addEventListener('click', (event) => {
			const checkedCount = document.querySelectorAll('input[name="emotionTags"]:checked').length;

			if (checkedCount > 1) {
				alert('비회원은 감정 태그를 1개만 선택할 수 있습니다.');
				event.target.checked = false;
			}
		});
	});
}
// main.js 파일의 적절한 위치에 이 코드를 추가합니다.

// --- 중복 값 태그 비활성화 로직 ---

// 1. name 속성을 기준으로 태그 그룹들을 정의합니다.
const tagGroups = ['situationTags', 'emotionTags'];

tagGroups.forEach(groupName => {
	const checkboxes = document.querySelectorAll(`input[name="${groupName}"]`);

	checkboxes.forEach(checkbox => {
		checkbox.addEventListener('change', function() {
			// 현재 클릭된 체크박스의 값 (예: "회식")
			const currentValue = this.value;
			// 현재 클릭된 체크박스의 상태 (체크됨/해제됨)
			const isChecked = this.checked;

			// 같은 그룹 내의 다른 모든 체크박스를 순회합니다.
			checkboxes.forEach(otherCheckbox => {
				// 자기 자신이 아니고, 값이 같다면
				if (otherCheckbox !== this && otherCheckbox.value === currentValue) {
					// 방금 체크박스를 '선택'했다면, 다른 동일 값 태그를 '비활성화'합니다.
					otherCheckbox.disabled = isChecked;
				}
			});
		});
	});
});

