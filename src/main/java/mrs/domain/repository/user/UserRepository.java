package mrs.domain.repository.user;

import mrs.domain.model.User;

public interface UserRepository {
	public User findByUserId(String userId);
}