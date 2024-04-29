package mrs.exception;

public class CustomException extends RuntimeException {

	// 非チェック例外 (RuntimeExceptionを直接または間接的に拡張する例外):RuntimeExceptionを拡張することで、メソッドのシグネチャにthrows句を含める必要がなくなる
	// →例外処理を強制されることはない。つまり、コンパイル時にチェックされない。
	// →通常、プログラミングエラーなどの予期しない状況を表すために使用される。

	// コンストラクタ
	public CustomException(String message) {
		super(message);
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}
}
