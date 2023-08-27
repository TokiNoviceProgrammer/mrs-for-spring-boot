package mrs.app.reservation;

import java.io.Serializable;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EndTimeMustBeAfterStartTime(message = "終了時刻は開始時刻より後にしてください")
public class ReservationForm implements Serializable {
	@NotNull(message = "必須です")
	@ThirtyMinutesUnit(message = "30分単位にしてください")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;

	@NotNull(message = "必須です")
	@ThirtyMinutesUnit(message = "30分単位にしてください")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;

}