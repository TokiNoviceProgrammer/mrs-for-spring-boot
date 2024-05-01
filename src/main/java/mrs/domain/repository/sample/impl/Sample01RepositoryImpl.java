package mrs.domain.repository.sample.impl;

import org.springframework.stereotype.Repository;

import mrs.domain.repository.sample.Sample01Mapper;
import mrs.domain.repository.sample.Sample01Repository;

@Repository
public class Sample01RepositoryImpl implements Sample01Repository {

	private final Sample01Mapper sample01Mapper;

	public Sample01RepositoryImpl(Sample01Mapper sample01Mapper) {
		this.sample01Mapper = sample01Mapper;
	}

	@Override
	public String selectSample(String str) {
		return this.sample01Mapper.selectSample(str);
	}

}
