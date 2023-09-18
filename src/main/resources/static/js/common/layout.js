console.log("layout.js 読み込み");


function checkBeforeAction(actionNm) {
	// 確認ダイアログを表示
	if (window.confirm(String(actionNm) + 'を実施します。よろしいですか？')) {
		// 「OK」時は実行
		return true;

	}
	else {
		// 「キャンセル」時の処理
		window.alert('キャンセルされました'); // 警告ダイアログを表示
		// 送信を中止
		return false;
	}
}