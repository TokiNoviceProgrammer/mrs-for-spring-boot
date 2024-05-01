package mrs.domain.service.sample.impl;

import java.util.Objects;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import mrs.domain.repository.sample.Sample01Repository;
import mrs.domain.service.sample.Sample01Service;
import mrs.exception.CustomException;

@Service
public class Sample01ServiceImpl implements Sample01Service {

	private final Sample01Repository sample01Repository;

	public Sample01ServiceImpl(Sample01Repository sample01Repository) {
		this.sample01Repository = sample01Repository;
	}

	@Override
	public String process01(String arg) {
		//		Class<? extends Object> classObject = new Object().getClass();
		//		String className = classObject.getEnclosingClass().getName();
		//		String methodName = classObject.getEnclosingMethod().getName();
		//
		//		System.out.println("Current Class is " + className);
		//		System.out.println("Current Method is " + methodName);
		Class<?> classObject = getClass(); // getClass() メソッドを使用する
		String className = classObject.getName();
		String methodName = "process01"; // メソッド名を直接指定

		StringBuilder sb = new StringBuilder();
		sb.append("Current Class is " + className);
		sb.append(", ");
		sb.append("Current Method is " + methodName);
		sb.append(", ");
		if (Objects.isNull(arg)) {
			sb.append("Argument is null.");
		} else {
			sb.append("Argument is " + arg);
			sb.append(".");
		}

		String resultSelect = this.selectSample("test");
		sb.append("The SQL execution result is 「" + resultSelect);
		sb.append("」.");

		String resultStr = sb.toString();

		return resultStr;
	}

	private String selectSample(String str) {
		try {
			return this.sample01Repository.selectSample(str);

		} catch (DataAccessException e) {
			// データアクセスに関する一般的な例外
			// CustomException にラップして再スロー
			throw new CustomException("「DataAccessException」が発生しました。", e);
		}
	}

}
