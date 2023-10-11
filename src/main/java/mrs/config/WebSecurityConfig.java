package mrs.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import mrs.app.login.CustomAuthenticationProvider;

@EnableMethodSecurity(prePostEnabled = true) // ReservationServiceのcancelで使用している@PreAuthorizeを有効化
@Configuration
@EnableWebSecurity // spring securityのweb連携機能(CSRF対策など)を有効にする
public class WebSecurityConfig {

	/**
	 * ユーザパスワード認証用のAuthenticationProviderを登録
	 * @param userDetailsService UserDetailsService
	 * @param passwordEncoder PasswordEncoderF
	 * @return AuthenticationProvider
	 */
	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		CustomAuthenticationProvider provider = new CustomAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setHideUserNotFoundExceptions(false);// UsernameNotFoundExceptionsがBadCredentialsExceptionに書き換えられないようにする
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		// PasswordEncoderをBeanとして登録すれば、アプリケーション共通のエンコード方法を指定できる
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// 認証リクエストの設定
				.authorizeHttpRequests(
						auth -> auth
								// 「cssやjs、imagesなどの静的リソース」をアクセス可能にする
								.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
								// 「/registerUser」と「/login」にはログインなしでもアクセス可能にする
								.requestMatchers("/registerUser", "/login", "/changePassword").permitAll()
								// 認証の必要があるように設定
								.anyRequest().authenticated())
				.formLogin((login) ->
				// ログイン画面・認証urlを設定
				login.loginPage("/").loginProcessingUrl("/login")
						// ユーザー名とパスワードのリクエストパラメータ名を設定
						.usernameParameter("username").passwordParameter("password")
						// 認証成功時と失敗時の遷移先を設定
						// defaultSuccessUrlの第二引数をtrueにし、認証成功時は常に指定したパスへ遷移する
						.defaultSuccessUrl("/rooms", true).failureHandler(this.failureHandler())
						// ログイン画面・認証url・認証失敗時の遷移先へのアクセスは常に許可する
						.permitAll());
		return http.build();
	}

	/**
	 * @return ログイン失敗時のハンドラーを返却
	 */
	private AuthenticationFailureHandler failureHandler() {
		return (request, response, exception) -> {
			AuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler("/");

			if (exception instanceof AccountExpiredException) {
				handler = new SimpleUrlAuthenticationFailureHandler("/changePassword");
			}

			handler.onAuthenticationFailure(request, response, exception);
		};
	}
}