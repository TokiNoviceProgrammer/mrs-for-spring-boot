package mrs.sample;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import mrs.domain.repository.sample.Sample01Repository;
import mrs.domain.service.sample.impl.Sample01ServiceImpl;
import mrs.exception.CustomException;

@SpringBootTest
@DisplayName("Sample01Serviceのテスト")
public class Sample01ServiceTest {

	@Mock
	private Sample01Repository sample01Repository;
	@InjectMocks //モックオブジェクトがインジェクションされる
	private Sample01ServiceImpl targetService;

	@Test
	@DisplayName("テストケース01")
	public void test01() {
		// テストケースの準備
		String argument = "test";
		String expected = "Current Class is mrs.domain.service.sample.impl.Sample01ServiceImpl, "
				+ "Current Method is process01, Argument is test."
				+ "The SQL execution result is 「sampleResult」.";

		// モックの設定
		when(sample01Repository.selectSample(any())).thenReturn("sampleResult");

		// テスト実行
		String result = this.targetService.process01(argument);

		// 結果の検証
		assertEquals(expected, result);
	}

	@Test
	@DisplayName("テストケース02")
	public void test02() {
		// テストケースの準備
		String argument = null;
		String expected = "Current Class is mrs.domain.service.sample.impl.Sample01ServiceImpl, "
				+ "Current Method is process01, Argument is null."
				+ "The SQL execution result is 「sampleResult」.";

		// モックの設定
		when(sample01Repository.selectSample(any())).thenReturn("sampleResult");

		// テスト実行
		String result = this.targetService.process01(argument);

		// 結果の検証
		assertEquals(expected, result);
	}

	@Test
	@DisplayName("テストケース03: DataAccessExceptionが発生する場合")
	public void test03_DataAccessException() {
		// リポジトリのモックを設定して例外を発生させる
		when(sample01Repository.selectSample(any())).thenThrow(new DataAccessException("mock exception") {
		});

		// テスト実行＆例外の検証
		assertThrows(CustomException.class, () -> targetService.process01("test"));
	}
}
