// 共通部品

/** フォーム内の変更状態を管理するフラグ */
let isFormChanged = false;

/**
 * 指定されたname属性を持つ要素のdisabled属性を切り替える
 * @param {string} elementName 要素のname属性
 * @param {boolean} isDisabled true: 無効化, false: 有効化
 */
function toggleElementDisabled(elementName, isDisabled) {
    $('input[name="' + elementName + '"]').prop('disabled', isDisabled);
}

/**
 * hhmm形式の文字列をhh:mm形式に変換する
 * @param {string} timeString hhmm形式の文字列
 * @return {string} 変換後の結果（hh:mm形式）
 */
function formatTimeToHhMm(timeString) {
    if (!timeString || typeof timeString !== 'string' || timeString.length !== 4) {
        return timeString;
    }
    return timeString.substr(0, 2) + ':' + timeString.substr(-2);
}

/**
 * セレクトボックスを未選択状態にする
 * @param {string} selectBoxId セレクトボックスのID
 */
function resetSelectBox(selectBoxId) {
    let selectBox = document.getElementById(selectBoxId);
    selectBox.selectedIndex = -1;
}

/**
 * セレクトボックスの連動イベントを定義する
 * @param {...Array<string>} selectBoxGroups 複数の連動するセレクトボックスIDのリストを指定
 */
function defineLinkedSelectBoxEvents(...selectBoxGroups) {
    $('select').change(function () {
        if (selectBoxGroups.length === 0) return;

        const changedSelectBoxId = this.id;
        selectBoxGroups.forEach(group => {
            if (!group.includes(changedSelectBoxId)) return;

            group.forEach(linkedSelectBoxId => {
                if (changedSelectBoxId === linkedSelectBoxId) return;

                const changedSelectBox = document.getElementById(changedSelectBoxId);
                const selectedValue = changedSelectBox.options[changedSelectBox.selectedIndex]?.value;

                let linkedSelectBox = document.getElementById(linkedSelectBoxId);
                for (let i = 0; i < linkedSelectBox.options.length; i++) {
                    if (linkedSelectBox.options[i].value === selectedValue) {
                        linkedSelectBox.options[i].selected = true;
                        break;
                    }
                }
            });
        });
    });
}

/**
 * Confirmダイアログを使ったフォーム送信の確認処理を定義する
 * @param {string} formId フォームのID
 * @param {string} buttonId ボタンのID
 * @param {string} messageId メッセージ表示用の要素ID
 * @param {function} beforeSubmitFn 送信前に実行する関数（オプション）
 * @param {function} customConfirmFn カスタム確認関数（オプション）
 * @param {function} afterSubmitFn 送信後に実行する関数（オプション）
 */
function defineConfirmEvent(formId, buttonId, messageId, beforeSubmitFn = () => { }, customConfirmFn = null, afterSubmitFn = () => { }) {
    const form = document.getElementById(formId);
    form.addEventListener('submit', event => {
        event.preventDefault();

        if (event.submitter.id !== buttonId) return;

        const confirmResult = customConfirmFn ? customConfirmFn() : displayConfirmDialog(messageId);
        if (confirmResult) {
            const formAction = document.activeElement.getAttribute("formaction") ||
                document.getElementById(buttonId).getAttribute("formaction") || form.action;

            form.action = formAction;
            beforeSubmitFn();
            form.submit();
            afterSubmitFn();
        }
    });
}

/**
 * 確認ポップアップを表示
 * @param {string} elementId 確認メッセージを表示する要素のID
 * @return {boolean} OK(true)/Cancel(false)
 */
function displayConfirmDialog(elementId) {
    const message = document.getElementById(elementId).value;
    return !message || window.confirm(message);
}

/**
 * フォームの変更状態を確認
 * @param {Event} event イベントオブジェクト
 * @return {boolean} 処理続行(true)/処理中断(false)
 */
function confirmFormChanges(event) {
    if (!isFormChanged) return true;

    if (window.confirm('フォームが変更されていますが続行しますか？')) {
        isFormChanged = false;
        return true;
    }

    event.preventDefault();
    return false;
}

/**
 * フォームの変更を監視し、変更があった場合にフラグをオンにする
 * @param {Array<string>} excludeIdList 監視から除外する要素IDのリスト
 * @param {string} requiredTextareaId 必須テキストエリアのID
 */
function observeFormChanges(excludeIdList = [], requiredTextareaId) {
    isFormChanged = false;

    $("form").change(event => {
        if (!excludeIdList.includes(event.target.id)) {
            isFormChanged = true;
        }
    });

    $("textarea").change(event => {
        const textareaContainer = document.getElementById(requiredTextareaId);
        if (textareaContainer && textareaContainer.contains(event.target)) {
            isFormChanged = true;
        }
    });
}

/**
 * 数値を3桁区切りに変換する
 * @param {string|number} value 変換対象の値
 * @return {string} カンマ区切りに変換された数値
 */
function formatNumberWithCommas(value) {
    const number = Number(value);
    return isNaN(number) ? "" : number.toLocaleString();
}

/**
 * テーブル行をクリックした際に色をつける
 * @param {Event} event イベントオブジェクト
 * @param {string} tableId テーブルのID
 */
function highlightTableRowOnClick(event, tableId) {
    $("#" + tableId + " tr td").css("background", "#ffffff");
    $(event.currentTarget).children("td").css("background", "#c8ffd9");
}

/**
 * Ajax通信で返却されたHTMLを使って画面を更新する
 * @param {string} htmlContent 返却されたHTML
 * @param {boolean} updateHead trueなら<head>要素も更新する
 */
function updatePageFromAjax(htmlContent, updateHead = false) {
    if (updateHead) {
        const headContent = extractContentBetween(htmlContent, "<head>", "</head>");
        $('head').html(headContent);
    }
    const bodyContent = extractContentBetween(htmlContent, "<body>", "</body>");
    $('body').html(bodyContent);
}

/**
 * 文字列から指定された範囲の部分文字列を抽出する
 * @param {string} targetStr 対象文字列
 * @param {string} startStr 開始文字列
 * @param {string} endStr 終了文字列
 * @return {string|null} 部分文字列またはnull
 */
function extractContentBetween(targetStr, startStr, endStr) {
    const startIndex = targetStr.indexOf(startStr);
    const endIndex = targetStr.indexOf(endStr, startIndex + startStr.length);

    if (startIndex !== -1 && endIndex !== -1) {
        return targetStr.substring(startIndex, endIndex + endStr.length);
    }
    return null;
}

/**
 * 指定された要素のmax-heightを変更する
 * @param {string} elementId 要素のID
 * @param {string} height 設定する高さ(px)
 */
function setMaxHeight(elementId, height) {
    const element = document.getElementById(elementId);
    element.style.maxHeight = height + 'px';
}

/**
 * エンターキーの無効化
 * テキストエリア内では許可する。
 */
function disableEnterKeyExceptInTextarea() {
    document.addEventListener("keydown", e => {
        if (e.key === 'Enter' && e.target.tagName !== 'TEXTAREA') {
            e.preventDefault();
        }
    });
}
