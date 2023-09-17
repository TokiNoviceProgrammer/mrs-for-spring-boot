package mrs.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import mrs.domain.service.user.ReservationUserDetails;

// 監査証跡で認証情報を利用するためにAuditorAwareを設定
@EnableJpaAuditing
@Configuration
public class SecurityAuditorAwareConfig {
	@Bean
	AuditorAware<String> auditorAware() {
		return new SecurityAuditorAware();
	}

	// AuditorAwareインタフェースは、Entity/Modelの操作者(作成者/更新者)を解決するためのもの
	private static class SecurityAuditorAware implements AuditorAware<String> {
		@Override
		public Optional<String> getCurrentAuditor() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || !authentication.isAuthenticated()) {
				return null;
			}
			ReservationUserDetails userDetails = (ReservationUserDetails) authentication.getPrincipal();

			return Optional.ofNullable(userDetails.getUsername());
		}
	}
}
