package mrs.app.login;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mrs.domain.model.TestModel;
import mrs.domain.service.user.ReservationUserDetailsService;

@Controller
public class LoginController {

	private final AuthenticationManager authenticationManager;
	private final ReservationUserDetailsService reservationUserDetailsService;

	LoginController(AuthenticationManager authenticationManager,
			ReservationUserDetailsService reservationUserDetailsService) {
		this.authenticationManager = authenticationManager;
		this.reservationUserDetailsService = reservationUserDetailsService;
	}

	@GetMapping("/")
	// ログインフォーム表示
	String loginForm() {
		return "login/loginForm";
	}

	@GetMapping("/customLoginForm")
	// パスワード変更表示
	String changePassword() {
		return "login/customLoginForm";
	}

	@PostMapping("customLogin")
	String customLogin(HttpServletRequest req,
			@RequestParam String username, // リクエストからユーザー名を取得
			@RequestParam String password) { // リクエストからパスワードを取得
		UserDetails userDetails = this.reservationUserDetailsService.loadUserByUsername(username);
		// パスワード変更後の認証用の新たなトークン作成
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				username,
				password,
				userDetails.getAuthorities());
		// 認証の実行を実施する
		// NOTE: Spring Securityの認証処理が実行され、
		// 認証が成功すればAuthenticationオブジェクト(認証されたユーザーの情報を表すインターフェース)を生成する
		// 認証に失敗すればログイン画面へリダイレクトされる
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		// Authenticationオブジェクトをセキュリティコンテキストに設定する
		// NOTE: Spring Securityにおけるセキュリティコンテキスト（Security Context）とは、
		// 認証情報（Authentication）や認可情報（Authorization）などのセキュリティに関連する情報を意味する
		// セキュリティコンテキストは、SecurityContextHolderを介してアクセスされ、
		// SecurityContextHolderはスレッドローカル変数として扱われる
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(authentication);
		// リクエストからセッションを取得(もしセッションが存在しない場合、trueを指定して新しいセッションを作成)
		HttpSession session = req.getSession(true);
		// セッションに認証情報を設定
		// NOTE: Spring SecurityはデフォルトでSPRING_SECURITY_CONTEXT_KEYというキーを使用し、
		// セキュリティコンテキストをセッションに保存する
		// これはブラウザとサーバー間でやり取りされ、ログイン状態を管理する
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

		// ログイン後の画面へリダイレクト
		return "redirect:/rooms";
	}

	@GetMapping("/system")
	// パスワード変更表示
	String system(Model model) {
		this.addTestModelList(model);
		return "login/system";
	}

	@PostMapping("/system")
	String receiveCheckedData(@RequestBody List<TestModel> testModelList, Model model) {
		if (this.checkedNgTestModelList(testModelList)) {
			return "error/400";
		}
		this.addTestModelList(model);
		return "login/system";
	}

	private void addTestModelList(Model model) {
		List<TestModel> testModelList = new ArrayList<>();

		TestModel testModel01 = new TestModel();
		testModel01.setNo("1");
		testModel01.setBranch("01");
		testModel01.setVal("テスト値1-01");
		testModel01.setKbn("10");
		testModelList.add(testModel01);

		TestModel testModel02 = new TestModel();
		testModel02.setNo("1");
		testModel02.setBranch("02");
		testModel02.setVal("テスト値1-02");
		testModel02.setKbn("11");
		testModelList.add(testModel02);

		TestModel testModel03 = new TestModel();
		testModel03.setNo("2");
		testModel03.setBranch("01");
		testModel03.setVal("テスト値2-01");
		testModel03.setKbn("20");
		testModelList.add(testModel03);

		model.addAttribute("testModelList", testModelList);
	}

	private boolean checkedNgTestModelList(List<TestModel> testModelList) {
		for (TestModel testModel : testModelList) {
			if (testModel.getBranch().length() > 2) {
				return true;
			}
		}
		return false;
	}
}