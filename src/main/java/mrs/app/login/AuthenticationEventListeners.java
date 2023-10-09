package mrs.app.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListeners {
	private static final Logger log = LoggerFactory.getLogger(AuthenticationEventListeners.class);

	// クライアントが指定した認証情報に誤りがあった場合に通知されるイベント
	@EventListener // @EventListenerを付与したメソッドを作成
	public void handleBadCredentials(
			AuthenticationFailureBadCredentialsEvent event) { // メソッドの引数にハンドリングしたい認証イベントクラスを指定
		// AuthenticationFailureBadCredentialsEvent: BadCredentialsExceptionが発生したことを通知するためのイベントクラス
		log.info("Bad credentials is detected. username : {}", event.getAuthentication().getName());
	}

	// クライアントが指定した認証情報がロックされている場合に通知されるイベント
	@EventListener
	public void handleLocked(
			AuthenticationFailureLockedEvent event) {
		// AuthenticationFailureLockedEvent: LockedExceptionが発生したことを通知するためのイベントクラス
		log.info("Locked is detected. username : {}", event.getAuthentication().getName());
	}
}