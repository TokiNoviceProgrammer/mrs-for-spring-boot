package mrs.app.login;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import mrs.domain.service.user.ReservationUserDetails;

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

		// 独自の認証
		ReservationUserDetails reservationUserDetails = (ReservationUserDetails) userDetails;
		if ("1".equals(reservationUserDetails.getUser().getInitFlg())) {
			throw new AccountExpiredException("パスワードを変更してください。");
		}

		// 既にログイン中であるにも関わらず、異なるユーザでログインしようとした場合は拒否する
		Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
		if (currentAuthentication != null && currentAuthentication.isAuthenticated()) {
			String currentUsername = currentAuthentication.getName(); // 現在認証されているユーザー名を取得
			String newUsername = reservationUserDetails.getUsername(); // 新たにログインしようと試みたユーザー名を取得
			if (!currentUsername.equals(newUsername)) {
				throw new BadCredentialsException("現在、ユーザ「" + currentUsername + "」でログイン中です");
			}

		}

	}

}