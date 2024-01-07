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

    function checkedCount() {
        const count = document.querySelectorAll('.checkedtd:checked').length;
        console.log('Checked count: ' + count);
        document.getElementById('checkedcount').textContent = count;
    }
});
