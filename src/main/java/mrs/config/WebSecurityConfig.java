package mrs.config;

import java.util.Collections;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import mrs.app.login.CustomAuthenticationProvider;
import mrs.domain.model.User;
import mrs.domain.service.user.ReservationUserDetails;

@EnableMethodSecurity(prePostEnabled = true) // ReservationServiceのcancelで使用している@PreAuthorizeを有効化
@Configuration
@EnableWebSecurity // spring securityのweb連携機能(CSRF対策など)を有効にする
public class WebSecurityConfig {

	/**
	 * 認証処理を実施に使用するためにbean登録する
	 *
	 * @param authenticationProvider 独自でbean登録した認証用のAuthenticationProvider
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationProvider authenticationProvider)
			throws Exception {
		// AuthenticationManagerの実装クラス(ProviderManager)のコンストラクタの引数にList<AuthenticationProvider>を変更不可のリストとして設定する
		return new ProviderManager(Collections.singletonList(authenticationProvider));
	}

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
								.requestMatchers("/registerUser", "/login", "/customLoginForm", "customLogin")
								.permitAll()
								// 認証の必要があるように設定
								.anyRequest().authenticated())
				.formLogin((login) ->
				// ログイン画面・認証urlを設定
				login.loginPage("/").loginProcessingUrl("/login")
						// ユーザー名とパスワードのリクエストパラメータ名を設定
						.usernameParameter("username").passwordParameter("password")
						// 認証成功時と失敗時の遷移先を設定
						// 成功時・失敗時のハンドラーは独自で実装して設定
						.successHandler(this.successHandler()).failureHandler(this.failureHandler())
						// ログイン画面・認証url・認証失敗時の遷移先へのアクセスは常に許可する
						.permitAll());
		return http.build();
	}

	/**
	 * @return ログイン成功時のハンドラーを返却
	 */
	private AuthenticationSuccessHandler successHandler() {
		return (request, response, exception) -> {
			AuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler("/rooms");

			// 資格情報を取得し、userIdがsysytemの場合は専用ページに遷移するようにする
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();
			ReservationUserDetails principal = (ReservationUserDetails) authentication.getPrincipal();
			User user = principal.getUser();
			if ("system".equals(user.getUserId())) {
				handler = new SimpleUrlAuthenticationSuccessHandler("/system");
			}

			handler.onAuthenticationSuccess(request, response, exception);
		};
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