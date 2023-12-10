package mrs.app.reservation.form;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestForm {
	@Size(min = 1, max = 3)
	private String id;
}
