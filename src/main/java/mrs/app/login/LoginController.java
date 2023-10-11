package mrs.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	@GetMapping("/")
	// ログインフォーム表示
	String loginForm() {
		return "login/loginForm";
	}

	@GetMapping("/changePassword")
	// パスワード変更表示
	String changePassword() {
		return "login/changePassword";
	}

	@GetMapping("/system")
	// パスワード変更表示
	String system() {
		return "login/system";
	}
}