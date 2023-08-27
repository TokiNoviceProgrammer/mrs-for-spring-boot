package mrs.app.reservation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = { ThirtyMinutesUnitValidator.class }) // チェックロジックはThirtyMinutesUnitValidatorクラスに委譲
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER }) // メソッド・フィールド・コンストラクタ・パラメータに使用可能にする、ANNOTATION_TYPE：アノテーション定義
@Retention(RUNTIME) // @Retantionでアノテーション情報をどの段階まで保持するかを制御(RUNTIME: JVMによって保持され、ランタイム環境で使用可能)
public @interface ThirtyMinutesUnit {
	// デフォルトのエラーメッセージを設定する
	// プロパティファイルから取得可能
	// クラスパス直下のValidationMessages.propertiesかmessages.propertise(spring bootのみ)に定義可能
	String message() default "{mrs.app.reservation.ThirtyMinutesUnit.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		ThirtyMinutesUnit[] value();
	}
}