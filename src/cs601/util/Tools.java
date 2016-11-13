package cs601.util;

import java.math.BigInteger;

/**
 * A util class to provide general static methods conveniently.
 */

public class Tools {
	
	
	

	/**
	 * Checks to see if a String is null or empty.
	 * @param text - String to check
	 * @return true if non-null and non-empty
	 */
	public static boolean isBlank(String text) {
		return (text == null) || text.trim().isEmpty();
	}
	
	
	
	
	
	
	/**
	 * Returns the hex encoding of a byte array.
	 * @param bytes - byte array to encode
	 * @param length - desired length of encoding
	 * @return hex encoded byte array
	 */
	public static String encodeHex(byte[] bytes, int length) {
		BigInteger bigint = new BigInteger(1, bytes);
		String hex = String.format("%0" + length + "X", bigint);

		assert hex.length() == length;
		return hex;
	}
	
	
	
	
}
