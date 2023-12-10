package mrs.app.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mrs.app.reservation.form.TestForm;

@RestController
@RequestMapping("testRest")
public class TestRestController {

	@PostMapping
	public ResponseEntity<Object> handleRequest(@Valid @RequestBody TestForm testForm) {
		// バリデーションが成功した場合の処理
		if ("99".equals(testForm.getId())) {
			// 警告が必要な場合: 202 Accepted
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body("警告あり");
		} else {
			// 警告が必要な場合: 200 OK
			return ResponseEntity.ok("警告なし");
		}
	}

	@PostMapping("/process")
	public ResponseEntity<Object> test(@Valid @RequestBody TestForm testForm) {
		// ここで何かしらの処理を実施する
		return ResponseEntity.ok("成功");
	}
}
