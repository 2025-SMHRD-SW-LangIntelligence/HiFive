// static/js/wishlist.js
function goBack() {
  window.location.href = "/mypage";
}



// ★ (핵심!) '찜 취소' 버튼에 대한 최신 로직만 남겨두거나 추가합니다. ★



// 'cancel-wishlist-btn' 클래스를 가진 모든 버튼을 찾습니다.
const cancelButtons = document.querySelectorAll('.cancel-wishlist-btn');

// 각 버튼에 클릭 이벤트를 추가합니다. 찜 취소 버튼에 대한 기능입니다.
cancelButtons.forEach(button => {
    button.addEventListener('click', function(event) {
        
        // 버튼이 가지고 있는 가게 ID('data-store-id')를 가져옵니다.
        const storeId = event.target.dataset.storeId;
        
        if (!confirm('정말로 찜을 취소하시겠어요?')) {
            return; // 사용자가 '취소'를 누르면 아무것도 하지 않음
        }

        // 1. 우리가 새로 만든 백엔드 API 주소로 요청을 보냅니다.
        //    (storeId를 쿼리 파라미터로 함께 보냅니다)
        fetch(`/api/wishlist/remove?storeId=${storeId}`, {
            method: 'DELETE' // 2. @DeleteMapping과 일치하도록 'DELETE' 방식으로 요청합니다.
        })
        .then(response => {
            if (response.ok) { // 3. 서버가 성공(200 OK) 신호를 보내면,
                alert('찜이 취소되었습니다.');
                location.reload(); // 4. (핵심!) 현재 페이지를 새로고침해서 즉시 결과를 반영합니다.
            } else {
                // 서버가 실패 신호(401, 500 등)를 보내면,
                // 실패 원인을 알아내기 위해 서버 응답 텍스트를 읽습니다.
                response.text().then(text => {
                    alert('찜 취소에 실패했습니다: ' + text);
                });
            }
        })
        .catch(error => {
            console.error('네트워크 에러:', error);
            alert('찜 취소 중 문제가 발생했습니다.');
        });
    });
});

