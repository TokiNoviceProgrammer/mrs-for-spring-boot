package mrs.app.reservation;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ThirtyMinutesUnitValidator implements ConstraintValidator<ThirtyMinutesUnit, LocalTime> {
	@Override
	public void initialize(ThirtyMinutesUnit constraintAnnotation) {
	}

	@Override
	public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
		if (value == null) {
			// nullの場合は他の@NotNullなどに委譲する
			return true;
		}
		// 30分単位か否かチェックした結果を返す
		return value.getMinute() % 30 == 0;
	}
}