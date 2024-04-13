package mrs.util;

public class MultiplicationUtil {
	/**
	 * 二つの整数を掛け算する
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static int multiplyIntegers(int num1, int num2) {
		return num1 * num2;
	}

	/**
	 * 二つの整数の配列を要素ごとに掛け算する
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static int[] multiplyIntegerArrays(int[] arr1, int[] arr2) {
		if (arr1.length != arr2.length) {
			throw new IllegalArgumentException("Arrays must have the same length");
		}

		int[] result = new int[arr1.length];
		for (int i = 0; i < arr1.length; i++) {
			result[i] = multiplyIntegers(arr1[i], arr2[i]);
		}
		return result;
	}
}
