package mrs.app.login;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * this class implements the authenticate method from AuthenticateProvider interface,
 * thus providing Spring with our custom logic for authenticating the user.
 * @author saurabhtiwari
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	UserDetailsService userDetailsService;

	private PasswordEncoder passwordEncoder;

	public CustomAuthenticationProvider() {
		this(PasswordEncoderFactories.createDelegatingPasswordEncoder());
	}

	public CustomAuthenticationProvider(PasswordEncoder passwordEncoder) {
		setPasswordEncoder(passwordEncoder);
	}

	/**
	 * the below authenticate method must provide an Auth object token once it has
	 * successfully authenticated the user. In case, it fails to authenticate the user
	 * it can return null. This means the request will be passed to next filter in line.
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName().trim();
		String password = authentication.getCredentials().toString().trim();
		String regexAlphaNum = "^[A-Za-z0-9]+$"; // 半角英数字のみ 

		if (!(this.checkLogic(regexAlphaNum, userName) && this.checkLogic(regexAlphaNum, password))) {
			throw new BadCredentialsException("半角英数字のみで入力してください");
		}

		UserDetails user = userDetailsService.loadUserByUsername(userName);

		if (Objects.isNull(user)) {
			throw new BadCredentialsException("指定のユーザは存在しません");
		}

		if (!this.passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("パスワードが誤っています");
		}

		Authentication auth = createSuccessfulAuthentication(authentication, user);

		return auth;
	}

	private Authentication createSuccessfulAuthentication(Authentication authentication, UserDetails user) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user,
				authentication.getCredentials(), user.getAuthorities());
		token.setDetails(authentication.getDetails());
		return token;
	}

	/**
	 * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
	 * not set, the password will be compared using
	 * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
	 * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
	 * types.
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * this method is important because it tells the spring security that which
	 * class of input Authentication this provider is capable of processing.
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	/**
	* 指定した正規表現で文字列のチェックを実施するためのメソッド。
	* @param regex チェック処理に使う正規表現パターン
	* @param target 検査対象文字列
	* @return result 検査対象が正規表現にマッチする場合はtrue、それ以外はfalse
	*/
	private boolean checkLogic(String regex, String target) {
		boolean result = true;
		if (target == null || target.isEmpty())
			return false;
		// 3. 引数に指定した正規表現regexがtargetにマッチするか確認する
		Pattern p1 = Pattern.compile(regex); // 正規表現パターンの読み込み
		Matcher m1 = p1.matcher(target); // パターンと検査対象文字列の照合
		result = m1.matches(); // 照合結果をtrueかfalseで取得
		return result;
	}
}