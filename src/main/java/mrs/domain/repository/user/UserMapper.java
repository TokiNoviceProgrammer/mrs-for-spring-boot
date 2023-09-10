package mrs.domain.repository.user;

import org.apache.ibatis.annotations.Mapper;

import mrs.domain.model.User;

@Mapper
public interface UserMapper {
	public User findByUserId(String userId);
}