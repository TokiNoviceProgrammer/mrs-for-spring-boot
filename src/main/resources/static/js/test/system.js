console.log("system.js 読み込み");
$(document).ready(function () {
    // allcheckboxのイベントリスナーを設定
    document.getElementById('allcheckbox').addEventListener('change', function () {
        // allcheckboxがチェックされたらテーブルデータのチェックボックスを全選択
        if (this.checked) {
            document.querySelectorAll('.checkedtd').forEach(function (checkbox) {
                checkbox.checked = true;
            });
        } else {
            // allcheckboxがチェックされていなくなったらテーブルデータのチェックボックスのチェックを全て外す
            document.querySelectorAll('.checkedtd').forEach(function (checkbox) {
                checkbox.checked = false;
            });
        }
        // チェック件数を更新
        checkedCount();
    });

    // テーブルデータのチェックボックスのイベントリスナーを設定
    document.querySelectorAll('.checkedtd').forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            // チェック件数を更新
            checkedCount();
        });
    });

    // ボタンがクリックされたときの処理
    document.getElementById('postbtn').addEventListener('click', function () {
        sendCheckedData();
    });
});

function checkedCount() {
    const count = document.querySelectorAll('.checkedtd:checked').length;
    console.log('Checked count: ' + count);
    document.getElementById('checkedcount').textContent = count;
}

// チェックボックスの状態が変更されたときの処理
function sendCheckedData() {
    let testModelList = [];
    document.querySelectorAll('.checkedtd:checked').forEach(function (checkbox) {
        let testModel = {
            no: checkbox.getAttribute('data-no'),
            branch: checkbox.getAttribute('data-branch'),
            val: checkbox.getAttribute('data-val'),
            kbn: checkbox.getAttribute('data-kbn')
        }
        testModelList.push(testModel);

    });
    if (testModelList.length === 0) {
        return;
    }
    // CSRFトークンを取得
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
    // AJAXを使用してサーバーサイドにデータを送信
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        beforeSend: function (xhr) {
            // CSRFトークンをリクエストヘッダーにセット
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        url: '/system', // サーバーサイドのエンドポイントを指定
        data: JSON.stringify(testModelList),
        success: function (response) {
            console.log('Data sent successfully');
        },
        error: function (error) {
            console.error('Error sending data:', error);
        }
    });
}
