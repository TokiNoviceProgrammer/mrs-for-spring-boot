console.log("action.js 読み込み");
// CSRFトークンを取得
const csrfToken = document.querySelector('meta[name="_csrf"]').content;
$(document).ready(function () {
    $('#test').on('click', function () {
        // フォームデータ
        const formData = {
            id: '99'
        };
        // バックエンドにリクエストを送信
        fetch('/testRest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            },
            body: JSON.stringify(formData),
        })
            .then(data => {
                if (data.status === 200) {
                    // 警告メッセージがない場合は、confirmなしで処理を続行
                    postAction();
                    return;
                }
                if (window.confirm("○○です、処理を続行しますか？")) {
                    // 警告メッセージがある場合は、confirmでokの場合のみ続行
                    postAction();
                }
            });
    });
});

function postAction() {
    // フォームデータ
    const formData = {
        id: '1'
    };
    fetch('/testRest/process', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(formData),
    })
        .then(data => {
            // サーバーからのレスポンスを処理
            // ex)リダイレクトなど
            const redirectUrl = '';
            window.location.href = redirectUrl;
        });
}