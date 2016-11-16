package cs601.util;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A util class to provide general static methods conveniently.
 */

public class Tools {
	
	
	/** convert Date to String with format "yyyy-MM-dd" */
	public static String toStringDate1(Date reviewDate){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(reviewDate);
		return date;
	}
	
	
	
	/** convert Date to String with format "MM:dd:yy" */
	public static String toStringDate2(Date reviewDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM:dd:yy");
		String date = formatter.format(reviewDate);
		return date;
	}
	
	
	
	/** convert Java Date to SQL Timestamp */
	public static Timestamp getTimestamp(Date date) {
		if (date == null) {
			return null;
		} else {
			Timestamp timestamp = new Timestamp(date.getTime());
			return timestamp;
		}
	}
	
	
	
	/** convert Java boolean to SQL Tinyint(1) */
	public static int toSQLBoolean(Boolean bool){
		if(bool){
			return 1;
		}else{
			return 0;
		}
	}
	
	

	/** Checks to see if a String is null or empty.	 */
	public static boolean isBlank(String text) {
		return (text == null) || text.trim().isEmpty();
	}
	
	
	
	
	
	/**  Returns the hex encoding of a byte array  */
	public static String encodeHex(byte[] bytes, int length) {
		BigInteger bigint = new BigInteger(1, bytes);
		String hex = String.format("%0" + length + "X", bigint);

		assert hex.length() == length;
		return hex;
	}
	
	
	
	
}
