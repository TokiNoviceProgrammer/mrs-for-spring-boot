package mrs.domain.service.user;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

import mrs.domain.model.User;
import mrs.domain.repository.user.UserRepository;

@Service
public class ReservationUserDetailsService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String password = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
				.getParameter("password");

		if ((Objects.isNull(username) || StringUtils.isEmpty(username))
				|| (Objects.isNull(password) || StringUtils.isEmpty(password))) {
			throw new UsernameNotFoundException("ユーザーとパスワードの両方を入力してください。");
		}

		User user = userRepository.findByUserId(username);
		if (Objects.isNull(user)) {
			throw new UsernameNotFoundException(username + " というユーザーは存在しません。");
		}
		return new ReservationUserDetails(user);
	}
}