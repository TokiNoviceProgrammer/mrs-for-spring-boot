package mrs.sample;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import mrs.domain.service.sample.Sample01Service;

@SpringBootTest
@DisplayName("Sample01Serviceのテスト")
public class Sample01ServiceTest {
	@Autowired
	private Sample01Service sample01Service;

	@Test
	@DisplayName("テストケース01")
	public void test01() {
		// テストケースの準備
		String argument = "test";
		String expected = "Current Class is mrs.domain.service.sample.impl.Sample01ServiceImpl, Current Method is process01, Argument is test.";

		// テスト実行
		String result = this.sample01Service.process01(argument);

		// 結果の検証
		assertEquals(expected, result);
	}

	@Test
	@DisplayName("テストケース02")
	public void test02() {
		// テストケースの準備
		String argument = null;
		String expected = "Current Class is mrs.domain.service.sample.impl.Sample01ServiceImpl, Current Method is process01, Argument is null.";

		// テスト実行
		String result = this.sample01Service.process01(argument);

		// 結果の検証
		assertEquals(expected, result);
	}
}
