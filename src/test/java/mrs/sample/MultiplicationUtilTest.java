package mrs.sample;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import mrs.util.MultiplicationUtil;

@SpringBootTest
@DisplayName("MultiplicationUtilのテスト")
public class MultiplicationUtilTest {

	@Test
	@DisplayName("二つの整数を掛け算するテスト")
	public void testMultiplyIntegers() {
		int num1 = 5;
		int num2 = 3;
		int expected = 15;

		int result = MultiplicationUtil.multiplyIntegers(num1, num2);

		assertEquals(expected, result);
	}

	@Test
	@DisplayName("二つの整数の配列を要素ごとに掛け算するテスト")
	public void testMultiplyIntegerArrays() {
		int[] arr1 = { 1, 2, 3, 4, 5 };
		int[] arr2 = { 2, 3, 4, 5, 6 };
		int[] expected = { 2, 6, 12, 20, 30 };

		int[] result = MultiplicationUtil.multiplyIntegerArrays(arr1, arr2);

		assertArrayEquals(expected, result);
	}

	@Test
	@DisplayName("二つの整数の配列の長さが異なる場合の例外テスト")
	public void testMultiplyIntegerArraysWithDifferentLengths() {
		int[] arr1 = { 1, 2, 3 };
		int[] arr2 = { 2, 3, 4, 5 };

		assertThrows(IllegalArgumentException.class, () -> {
			MultiplicationUtil.multiplyIntegerArrays(arr1, arr2);
		});
	}

	//	@Test
	//	@DisplayName("multiplyIntegersをモック化してテスト")
	//	public void testMultiplyIntegerArraysMock() {
	//		// MultiplicationUtil util = mock(MultiplicationUtil.class);
	//		// when(util.multiplyIntegers(anyInt(), anyInt())).thenAnswer(invocation -> {
	//		//     int arg1 = invocation.getArgument(0);
	//		//     int arg2 = invocation.getArgument(1);
	//		//     return arg1 * arg2;
	//		// });
	//
	//		// multiplyIntegers をモック化
	//		when(MultiplicationUtil.multiplyIntegers(1, 1)).thenReturn(1);
	//
	//		// テストケースの準備
	//		int[] arr1 = { 1, 1, 1, 1, 1 };
	//		int[] arr2 = { 1, 1, 1, 1, 1 };
	//		int[] expected = { 1, 1, 1, 1, 1 };
	//
	//		// テスト実行
	//		int[] result = MultiplicationUtil.multiplyIntegerArrays(arr1, arr2);
	//
	//		// 結果の検証
	//		assertArrayEquals(expected, result);
	//	}

}
