package mrs.config;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import mrs.domain.service.user.ReservationUserDetails;

@Aspect
@Component
public class MapperAspect {

	@Before("execution(* mrs.domain.repository..*Mapper.save*(..)) || " +
			"execution(* mrs.domain.repository..*Mapper.update*(..))")
	public void setCommonProperty(JoinPoint jp) throws Throwable {

		// Mapperのメソッド名を取得
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		String methodName = method.getName();

		// Spring Securityの認証情報を取得
		ReservationUserDetails loginUserInfo = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() instanceof ReservationUserDetails) {
			loginUserInfo = ReservationUserDetails.class.cast(authentication.getPrincipal());
		}

		// 現在日時の取得
		LocalDateTime now = LocalDateTime.now();

		// Mapperの第一引数（モデルオブジェクト）を取得
		Object[] args = jp.getArgs();
		Object dto = args[0];

		// save*メソッドは作成者・作成日時・更新者・更新日時をセット
		if (methodName.startsWith("save")) {
			setCreatedProperty(loginUserInfo, now, dto);
			setUpdatedProperty(loginUserInfo, now, dto);
			// update*メソッドは更新者・更新日時をセット
		} else if (methodName.startsWith("update")) {
			setUpdatedProperty(loginUserInfo, now, dto);
		}

	}

	// 作成者IDと作成日時をセット
	private void setCreatedProperty(ReservationUserDetails loginUserInfo, LocalDateTime now, Object dto)
			throws Throwable {

		// Mapperの引数にsetCreatedByIdメソッドがある場合、認証情報をセット
		Method setCreatedId = ReflectionUtils.findMethod(dto.getClass(), "setCreatedId", String.class);
		if (setCreatedId != null) {
			setCreatedId.invoke(dto, loginUserInfo.getUsername());
		}

		// Mapperの引数にsetCreatedTimestampメソッドがある場合、現在日時をセット
		Method setCreatedAt = ReflectionUtils.findMethod(dto.getClass(), "setCreatedAt", LocalDateTime.class);
		if (setCreatedAt != null) {
			setCreatedAt.invoke(dto, now);
		}

	}

	// 更新者IDと更新日時をセット
	private void setUpdatedProperty(ReservationUserDetails loginUserInfo, LocalDateTime now, Object dto)
			throws Throwable {

		// Mapperの引数にsetUpdatedByIdメソッドがある場合、認証情報をセット
		Method setUpdatedId = ReflectionUtils.findMethod(dto.getClass(), "setUpdatedId", String.class);
		if (setUpdatedId != null) {
			setUpdatedId.invoke(dto, loginUserInfo.getUsername());
		}

		// Mapperの引数にsetUpdatedTimestampメソッドがある場合、現在日時をセット
		Method setUpdatedAt = ReflectionUtils.findMethod(dto.getClass(), "setUpdatedAt", LocalDateTime.class);
		if (setUpdatedAt != null) {
			setUpdatedAt.invoke(dto, now);
		}

	}

}
