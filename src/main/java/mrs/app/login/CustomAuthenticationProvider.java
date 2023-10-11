package mrs.app.login;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * カスタム認証プロバイダ<br>
 * Spring Securityのデフォルト実装であるDaoAuthenticationProviderを継承したクラス
 * 
 * @author 鴇崎
 */
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		try {
			super.additionalAuthenticationChecks(userDetails, authentication);
		} catch (BadCredentialsException ex) {
			throw new BadCredentialsException("パスワード認証に失敗しました。");
		}
	}

}