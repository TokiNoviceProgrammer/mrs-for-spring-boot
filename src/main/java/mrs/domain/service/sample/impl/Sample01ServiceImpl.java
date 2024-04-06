package mrs.domain.service.sample.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import mrs.domain.service.sample.Sample01Service;

@Service
public class Sample01ServiceImpl implements Sample01Service {

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

		String resultStr = sb.toString();
		System.out.println(resultStr);

		return resultStr;
	}

}
