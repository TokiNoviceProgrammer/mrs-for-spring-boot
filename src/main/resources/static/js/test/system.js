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
            // 新しいHTMLからheadとbodyを抽出
            const head = extractStringBetween(response, "<head>", "</head>");
            const body = extractStringBetween(response, "<body>", "</body>");
            // 既存のheadとbodyを差し替え
            $('head').html(head);
            $('body').html(body);
        },
        error: function (error) {
            console.error('Error sending data:', error);
        }
    });
}
/**
 * 対象文字列内から開始文字列と終了文字列の間にある部分文字列を取出す
 * @param {string} targetStr 対象文字列
 * @param {string} startStr 開始文字列
 * @param {string} endStr 終了文字列
 * @returns ex) targetStr:'aaaSbbbEccc'、startStr:'S'、endStr:'E' → 'SbbbE'
 */
function extractStringBetween(targetStr, startStr, endStr) {
    const startIndex = targetStr.indexOf(startStr);
    const endIndex = targetStr.indexOf(endStr, startIndex + startStr.length);
    let result = startStr;
    if (startIndex !== -1 && endIndex !== -1) {
        result += targetStr.substring(startIndex + startStr.length, endIndex);
        result += endStr
        return result;
    }
    return null; // 対象文字列内に開始文字列と終了文字列が存在しない場合はnullを返却する
}