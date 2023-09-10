package mrs.domain.repository.user;

import org.springframework.stereotype.Repository;

import mrs.domain.model.User;

@Repository
public class UserRepositoryImpl implements UserRepository {
	private final UserMapper userMapper;

	public UserRepositoryImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public User findByUserId(String userId) {
		return userMapper.findByUserId(userId);
	}

}