package androidutility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;


public class PrimUtil {

	/**
	 * Determine if the current s is a boolean value
	 * @param s
	 * @return
	 */
	public static boolean isBoolean(String s){
		List<String> bools = new ArrayList<String>(Arrays.asList("" +
				"true",
				"false",
				"True",
				"False",
				"TRUE",
				"FALSE"));
		return bools.contains(s);
	}
	/**
	 * Determine if the current s is an integer or not
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isInteger(String s) {
		if (s == null || s.length() == 0)	return false;
		for (int i = 0; i < s.length(); i++) {
			if (i == 0 && s.charAt(i) == '-')
				continue;
			if (s.charAt(i) > '9' || s.charAt(i) < '0')
				return false;
		}
		return true;
	}
	
	/**
	 * Determine if the current s is a positive integer or not
	 * @param s
	 * @return
	 */
	public static boolean isNonNegativeInteger(String s){
		if (s == null || s.length() == 0)	return false;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) > '9' || s.charAt(i) < '0')
				return false;
		}
		return true;
	}
	
	public static boolean isNonPositiveInteger(String s){
		if (s == null || s.length() == 0 || s.charAt(0) != '-')	return false;
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) > '9' || s.charAt(i) < '0')
				return false;
		}
		return true;
	}

	/**
	 * Note: the decimal here doesn't contain integer, i.e., we consider 1.0,
	 * 0.1, .3 as decimals, rather than 1, 3 or 4
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isDecimal(String s) {
		if (s == null || s.length() == 0)	return false;
		int dot = s.indexOf('.');
		//If there's no occurence of ., return false;
		if (dot == -1)	return false;
		//If there are many occurrences of ., return false;
		if (s.indexOf('.', dot + 1) != -1)	return false;
		
		return isInteger(s.substring(0, dot - 1)) && isNonNegativeInteger(s.substring(dot + 1));
		
	}

	/**
	 * Determine if the current s is a scientific notation or not
	 * The format of scientific notation is as follows:
	 * 1e19, 3.1e3, 1e-2, .3e4, 3E4E5E
	 * @param s
	 * @return
	 */
	public static boolean isScientificNotation(String s){
		if (s == null || s.length() == 0)	return false;
		String[] arr = s.toLowerCase().split("e");
		if (arr.length == 0 || !(isDecimal(arr[0]) || isInteger(arr[0])))	return false;
		for (int i = 1 ; i < arr.length; i++){
			if (!isInteger(arr[i]))
				return false;
		}
		return true;
	}
	
	/**
	 * Determine if the current string s is a digital or not The format of
	 * digital should contains 
	 * 1) 1,-1 -integer 
	 * 2) 1.1, -.2, -0.1, 11.33 -long
	 * 3) 10e1, 2e-2, 3E4E5 -scientific notations
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isDigital(String s) {
		return isScientificNotation(s) || isDecimal(s) || isInteger(s);
	}

	/**
	 * Determine if the current str is a predictable string or not
	 * 
	 * @param t
	 * @return
	 */
	public static boolean isPredictable(String t) {

		// Decoding the t if it is encoded with URLEncoder
		/*try {
			t = CryptoUtil.urlDecoding(t);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (isBase64(t)) {
			t = CryptoUtil.base64Decoding(t);
		}

		// Rules of predictable value
		if (isBoolean(t))	return true;
		if (LocaleUtil.isLocale(t))	return true;
		
		// Length of string is too short. [a-zA-Z0-9_] 63^3 = 250047
		if (t.length() < 4)
			return true;

		return false;
	}

	/**
	 * Determine if the current character is upper case or not
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isCharUpper(char c) {
		return c >= 'A' && c <= 'Z';
	}

	/**
	 * Determine if the current character is lower case or not
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isCharLower(char c) {
		return c >= 'a' && c <= 'z';
	}

	/**
	 * Check if a printable string is encoded by Base64 or not
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBase64(String str) {
		Pattern pattern = Pattern
				.compile("^(?:[A-Za-z0-9+/_-]{4})*(?:[A-Za-z0-9+/_-]{2}==|[A-Za-z0-9+/_-]{3}=)?$");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	/**
	 * Determine if a char is an ASCII character or not
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isASCII(char c) {
		return (c & (1 << 7)) == 0;
	}
	
	/**
	 * Determine if the string is a url or not
	 * @param str
	 * @return
	 */
	public static boolean isUrl(String str) {
		UrlValidator urlVal = new UrlValidator();
		return urlVal.isValid(str);
	}
	
	/**
	 * Check if one double value equals to zero
	 * @param obj
	 * @param precision
	 * @return
	 */
	public static boolean equalsToZero(double obj, int precision){
		return Math.abs(obj) <= Math.pow(0.1, precision);
	}
	
	public static boolean equalsToZero(double obj){
		return equalsToZero(obj, 6);
	}
	/**
	 * Check if one double value equals to a specific value
	 * @param obj
	 * @param value
	 * @return
	 */
	public static boolean equalsTo(double obj, double value){
		return equalsTo(obj, value, 6);
	}
	
	public static boolean equalsTo(double obj, double value, int precision){
		return Math.abs(obj - value) <= Math.pow(0.1, precision);
	}
}