package androidutility;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import org.apache.commons.validator.routines.EmailValidator;
/**
 * # Device info [1] low bandwidth sensors (info that often changes frequently
 * and is simultaneously used by multiple applications)
 * 
 * • Location • accelerometer
 * 
 * high bandwidth sensors (each request from the sensor frequently returns a
 * large amount of data that is only used by one application)
 * 
 * info databases device identifiers (info that uniquely identifies the phone or
 * the user)
 * 
 * • Microphone • camera • address book/contacts • SMS • phone number • SIM card
 * identifiers • IMSI: unique 15-digit code used to identify an individual •
 * ICC-ID number: unique SIM card serial number • device identifier (IMEI):
 * uniquely identifies a specific mobile
 * 
 * phone and is used to prevent a stolen handset from accessing the cellular
 * network user on a GSM network
 * 
 * App info
 * 
 * [2]
 * 
 * • Bank account information, payment credentials for Paypal, American Express,
 * Diners Clubs and others • Facebook, email and cloud storage credentials and
 * messages • Twitter, Google, Yahoo, Microsoft Live ID, Box, WordPress
 * credentials
 * 
 * [3]
 * 
 * • Contact list, conversation history, chat messages in popular instant
 * messengers such as MSN • Authentication tokens or AuthToken: can be used to
 * automatically sign
 * 
 * into a remote website without entering passwords
 * 
 * • browser history and bookmarks • incoming and outgoing phone call logs
 * 
 * @author pillar
 * 
 */
public class AndroidInfoUtil {

	// Mobile Country Code
	// http://en.wikipedia.org/wiki/Mobile_country_code
	public static final int[] MCC = { 202, 204, 206, 208, 212, 213, 214, 216,
			218, 219, 220, 222, 225, 226, 228, 230, 231, 232, 234, 235, 238,
			240, 242, 244, 246, 247, 248, 250, 255, 257, 259, 260, 262, 266,
			268, 270, 272, 274, 276, 278, 280, 282, 283, 284, 286, 288, 290,
			292, 293, 294, 295, 297, 302, 308, 310, 311, 312, 313, 314, 315,
			316, 330, 332, 334, 338, 340, 340, 342, 344, 346, 348, 350, 352,
			354, 356, 358, 360, 362, 363, 364, 365, 366, 368, 370, 372, 374,
			376, 400, 401, 402, 404, 405, 406, 410, 412, 413, 414, 415, 416,
			417, 418, 419, 420, 421, 422, 424, 425, 425, 426, 427, 428, 429,
			430, 431, 432, 434, 436, 437, 438, 440, 441, 450, 452, 454, 455,
			456, 457, 460, 461, 466, 467, 470, 472, 502, 505, 510, 514, 515,
			520, 525, 528, 530, 534, 535, 536, 537, 539, 540, 541, 542, 543,
			544, 545, 546, 547, 548, 549, 550, 551, 552, 555, 602, 603, 604,
			605, 606, 607, 608, 609, 610, 611, 612, 613, 614, 615, 616, 617,
			618, 619, 620, 621, 622, 623, 624, 625, 626, 627, 628, 629, 630,
			631, 632, 633, 634, 635, 636, 637, 638, 639, 640, 641, 642, 643,
			645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 657, 702,
			704, 706, 708, 710, 712, 714, 716, 722, 724, 730, 732, 734, 736,
			738, 740, 742, 744, 746, 748, 750 };

	/**
	 * Check if the number is IMEI number
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isIMEI(String number) {
		if (number == null)
			return false;
		if (PrimUtil.isNonNegativeInteger(number) && number.length() == 15){
			return CryptoUtil.luhnChecker(number);
		}
		return false;		
	}

	/**
	 * Check if the number is IMSI number
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isIMSI(String number) {
		if (number == null)
			return false;
		if (PrimUtil.isNonNegativeInteger(number) && number.length() == 15) {
			int m = Integer.parseInt(number.substring(0, 3));
			return (Arrays.binarySearch(MCC, m) != -1);
		}
		return false;
	}

	/**
	 * Check if the url is a validated url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isBrowserHistory(String url) {
		return PrimUtil.isUrl(url);
	}
	
	/**
	 * Check if the string is a phone number
	 * @param number
	 * @return
	 */
	public static boolean isPhoneNumber(String number){
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		boolean isValid = false;
		try 
		{
			Set<String> set = phoneUtil.getSupportedRegions();

			String[] arr = set.toArray(new String[set.size()]);

			for (int i = 0; i < set.size(); i++) 
			{
			    Locale locale = new Locale("en", arr[i]);
			    PhoneNumber phoneNum = phoneUtil.parse(number, locale.getDisplayCountry());
			    isValid = phoneUtil.isValidNumber(phoneNum);
			    if(isValid)
			    	return isValid;
			}
		} 
		catch (NumberParseException e) 
		{
			  System.err.println("NumberParseException was thrown: " + e.toString());
		}
		return isValid;
	}
	/**
	 * Check if the string is a location
	 * @param location
	 * @return
	 */
	public static boolean isLocation(String location){
		return false;
	}
	
	/**
	 * Check if the string is an email
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email)
	{
		return EmailValidator.getInstance().isValid(email);
	}
}