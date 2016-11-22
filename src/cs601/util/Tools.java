package cs601.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Random;

/**
 * A util class - provide static methods to deal with data type conversion, format data, etc.
 */

public class Tools {
	
	
	
	/** Checks if a given String is a null or empty String.*/
	public static boolean isBlank(String text) {
		return (text == null) || text.trim().isEmpty();
	}
	
	
	
	
	/**  format double with two decimals when displayed */
	@SuppressWarnings("resource")
	public static String formatDouble(double value){
		return new Formatter().format("%.2f", value).toString();  
	}
	
	
	
	
	
	/*-----------------------------------------Date Format and Type convert-----------------------------------------*/
	
	/** convert Date to String with format "yyyy-MM-dd" */
	public static String toStringDate(Date reviewDate){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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
	
	
	/** get current date */
	public static String getDate() {
		String format = "hh:mm a 'on' EEE, MMM dd, yyyy";
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(Calendar.getInstance().getTime());
	}
	
	
	
	/** get current date */
	public static String getDate2() {
		String format = "yyyyMMddHHmmssZ";
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	
	
	/*-------------------------------------------------encode password---------------------------------------------*/
	
	
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
			hashed = encodeHex(md.digest(), 64);
		} catch (Exception e) {
			System.out.println("Unable to properly hash password." + e);
		}
		return hashed;
	}
	
	
	
	/** generate a unique reviewId for every review. */
	public static String getUniqueId(String date){
		byte[] saltBytes = new byte[16];
		Random random = new Random(System.currentTimeMillis());
		random.nextBytes(saltBytes);
		String salt = encodeHex(saltBytes, 32);
		String hashed = getHash(date, salt);
		
		return hashed;
	}
	
	
	
	/*------------------convert the data type of Recommendation info beween int, boolean, String----------------------*/
	
	/** convert boolean to int(1/0)  */
	public static int bool2int(Boolean bool){
		if(bool){
			return 1;
		}else{
			return 0;
		}
	}
	
	
	
	/** convert int(1/0) to boolean */
	public static boolean int2bool(int isRecom){
		if(isRecom == 1){
			return true;
		}else{
			return false;
		}
	}
	
	
	/** convert boolean to String YES/NO */
	public static String bool2yn(boolean isRecom){
		if(isRecom){
			return "YES";
		}else{
			return "NO";
		}
	}
	
	/** convert String YES/NO to int */
	public static int yn2int(String isRecom){
		if(isRecom.equals("YES")){
			return 1;
		}else{
			return 0;
		}
	}
	
	

	
	
	
	
	
	
}
