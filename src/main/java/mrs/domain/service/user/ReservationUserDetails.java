package mrs.domain.service.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import mrs.domain.model.User;

// spring securityで使用する認証ユーザーを定義する
// ReservationUserDetailsは、mrs.domain.model.Userを内包したUserDetails実装クラス
public class ReservationUserDetails implements UserDetails {
	private final User user;

	public ReservationUserDetails(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	// RoleName型のenumをspring securityのGrantedAuthorityに変換する
	// 変換する際には、プレフィックスに「ROLE_」が必要になる
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_" + this.user.getRoleName().name());
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserId();
	}

	////////////////////////////////////////////////
	// アカウント期限切れ・アカウントロック・パスワード有効期限切れ・アカウント無効化に関するプロパティは使用しないようにする
	////////////////////////////////////////////////
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}