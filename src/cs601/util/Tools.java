package cs601.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

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
	
	
	/**  two decimals */
	@SuppressWarnings("resource")
	public static String formatDouble(double value){
		return new Formatter().format("%.2f", value).toString();  
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
	
	
	/*-----------------------------------------isRecom---------------------------------------------*/
	
	/** convert Java boolean to SQL Tinyint(1) */
	public static int toSQLBoolean(Boolean bool){
		if(bool){
			return 1;
		}else{
			return 0;
		}
	}
	
	/** convert SQL Tinyint(1) to boolen */
	public static boolean intToBool(int isRecom){
		if(isRecom == 1){
			return true;
		}else{
			return false;
		}
	}
	
	
	/** convert boolean to String YES/NO */
	public static String boolToString(boolean isRecom){
		if(isRecom){
			return "YES";
		}else{
			return "NO";
		}
	}
	
	
	public static int YNToInt(String isRecom){
		if(isRecom.equals("YES")){
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
	
	
	/** Calculates the hash of a password and hashed salt using SHA-256. */
	public static String getHash(String password, String salt) {
		
		String salted = salt + password;
		String hashed = salted;
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salted.getBytes());
			hashed = Tools.encodeHex(md.digest(), 64);
		} catch (Exception ex) {
			System.out.println("Unable to properly hash password." + ex);
		}
		return hashed;
	}
	
	
}
