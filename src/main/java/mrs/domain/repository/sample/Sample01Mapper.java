package mrs.domain.repository.sample;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Sample01Mapper {
	public String selectSample();
}
