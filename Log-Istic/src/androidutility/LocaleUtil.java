package androidutility;

import java.util.HashMap;

public class LocaleUtil {
	
	public static class Locale{
		private String language;
		private String country;
		private String localeId;
		
		public Locale(String language, String country, String localeId){
			this.language = language;
			this.country = country;
			this.localeId = localeId;
		}
	}
	
	public static HashMap<String, Locale> locales= new HashMap<String, Locale>();
	
	static{	
		//http://www.oracle.com/technetwork/java/javase/javase7locales-334809.html
		locales.put("sq_AL", new Locale("Albanian", "Albania", "sq_AL"));
		locales.put("ar_DZ", new Locale("Arabic", "Algeria", "ar_DZ"));
		locales.put("ar_BH", new Locale("Arabic", "Bahrain", "ar_BH"));
		locales.put("ar_EG", new Locale("Arabic", "Egypt", "ar_EG"));
		locales.put("ar_IQ", new Locale("Arabic", "Iraq", "ar_IQ"));
		locales.put("ar_JO", new Locale("Arabic", "Jordan", "ar_JO"));
		locales.put("ar_KW", new Locale("Arabic", "Kuwait", "ar_KW"));
		locales.put("ar_LB", new Locale("Arabic", "Lebanon", "ar_LB"));
		locales.put("ar_LY", new Locale("Arabic", "Libya", "ar_LY"));
		locales.put("ar_MA", new Locale("Arabic", "Morocco", "ar_MA"));
		locales.put("ar_OM", new Locale("Arabic", "Oman", "ar_OM"));
		locales.put("ar_QA", new Locale("Arabic", "Qatar", "ar_QA"));
		locales.put("ar_SA", new Locale("Arabic", "Saudi Arabia", "ar_SA"));
		locales.put("ar_SD", new Locale("Arabic", "Sudan", "ar_SD"));
		locales.put("ar_SY", new Locale("Arabic", "Syria", "ar_SY"));
		locales.put("ar_TN", new Locale("Arabic", "Tunisia", "ar_TN"));
		locales.put("ar_AE", new Locale("Arabic", "United Arab Emirates", "ar_AE"));
		locales.put("ar_YE", new Locale("Arabic", "Yemen", "ar_YE"));
		locales.put("be_BY", new Locale("Belarusian", "Belarus", "be_BY"));
		locales.put("bg_BG", new Locale("Bulgarian", "Bulgaria", "bg_BG"));
		locales.put("ca_ES", new Locale("Catalan", "Spain", "ca_ES"));
		locales.put("zh_CN", new Locale("Chinese (Simplified)", "China", "zh_CN"));
		locales.put("zh_SG(*)", new Locale("Chinese (Simplified)", "Singapore", "zh_SG(*)"));
		locales.put("zh_HK", new Locale("Chinese (Traditional)", "Hong Kong", "zh_HK"));
		locales.put("zh_TW", new Locale("Chinese (Traditional)", "Taiwan", "zh_TW"));
		locales.put("hr_HR", new Locale("Croatian", "Croatia", "hr_HR"));
		locales.put("cs_CZ", new Locale("Czech", "Czech Republic", "cs_CZ"));
		locales.put("da_DK", new Locale("Danish", "Denmark", "da_DK"));
		locales.put("nl_BE", new Locale("Dutch", "Belgium", "nl_BE"));
		locales.put("nl_NL", new Locale("Dutch", "Netherlands", "nl_NL"));
		locales.put("en_AU", new Locale("English", "Australia", "en_AU"));
		locales.put("en_CA", new Locale("English", "Canada", "en_CA"));
		locales.put("en_IN", new Locale("English", "India", "en_IN"));
		locales.put("en_IE", new Locale("English", "Ireland", "en_IE"));
		locales.put("en_MT(*)", new Locale("English", "Malta", "en_MT(*)"));
		locales.put("en_NZ", new Locale("English", "New Zealand", "en_NZ"));
		locales.put("en_PH(*)", new Locale("English", "Philippines", "en_PH(*)"));
		locales.put("en_SG(*)", new Locale("English", "Singapore", "en_SG(*)"));
		locales.put("en_ZA", new Locale("English", "South Africa", "en_ZA"));
		locales.put("en_GB", new Locale("English", "United Kingdom", "en_GB"));
		locales.put("en_US", new Locale("English", "United States", "en_US"));
		locales.put("et_EE", new Locale("Estonian", "Estonia", "et_EE"));
		locales.put("fi_FI", new Locale("Finnish", "Finland", "fi_FI"));
		locales.put("fr_BE", new Locale("French", "Belgium", "fr_BE"));
		locales.put("fr_CA", new Locale("French", "Canada", "fr_CA"));
		locales.put("fr_FR", new Locale("French", "France", "fr_FR"));
		locales.put("fr_LU", new Locale("French", "Luxembourg", "fr_LU"));
		locales.put("fr_CH", new Locale("French", "Switzerland", "fr_CH"));
		locales.put("de_AT", new Locale("German", "Austria", "de_AT"));
		locales.put("de_DE", new Locale("German", "Germany", "de_DE"));
		locales.put("de_LU", new Locale("German", "Luxembourg", "de_LU"));
		locales.put("de_CH", new Locale("German", "Switzerland", "de_CH"));
		locales.put("el_CY(*)", new Locale("Greek", "Cyprus", "el_CY(*)"));
		locales.put("el_GR", new Locale("Greek", "Greece", "el_GR"));
		locales.put("iw_IL", new Locale("Hebrew", "Israel", "iw_IL"));
		locales.put("hi_IN", new Locale("Hindi", "India", "hi_IN"));
		locales.put("hu_HU", new Locale("Hungarian", "Hungary", "hu_HU"));
		locales.put("is_IS", new Locale("Icelandic", "Iceland", "is_IS"));
		locales.put("in_ID(*)", new Locale("Indonesian", "Indonesia", "in_ID(*)"));
		locales.put("ga_IE(*)", new Locale("Irish", "Ireland", "ga_IE(*)"));
		locales.put("it_IT", new Locale("Italian", "Italy", "it_IT"));
		locales.put("it_CH", new Locale("Italian", "Switzerland", "it_CH"));
		locales.put("ja_JP", new Locale("Japanese (Gregorian calendar)", "Japan", "ja_JP"));
		locales.put("ja_JP_JP", new Locale("Japanese (Imperial calendar)", "Japan", "ja_JP_JP"));
		locales.put("ko_KR", new Locale("Korean", "South Korea", "ko_KR"));
		locales.put("lv_LV", new Locale("Latvian", "Latvia", "lv_LV"));
		locales.put("lt_LT", new Locale("Lithuanian", "Lithuania", "lt_LT"));
		locales.put("mk_MK", new Locale("Macedonian", "Macedonia", "mk_MK"));
		locales.put("ms_MY(*)", new Locale("Malay", "Malaysia", "ms_MY(*)"));
		locales.put("mt_MT(*)", new Locale("Maltese", "Malta", "mt_MT(*)"));
		locales.put("no_NO", new Locale("Norwegian (Bokmål)", "Norway", "no_NO"));
		locales.put("no_NO_NY", new Locale("Norwegian (Nynorsk)", "Norway", "no_NO_NY"));
		locales.put("pl_PL", new Locale("Polish", "Poland", "pl_PL"));
		locales.put("pt_BR(*)", new Locale("Portuguese", "Brazil", "pt_BR(*)"));
		locales.put("pt_PT(*)", new Locale("Portuguese", "Portugal", "pt_PT(*)"));
		locales.put("ro_RO", new Locale("Romanian", "Romania", "ro_RO"));
		locales.put("ru_RU", new Locale("Russian", "Russia", "ru_RU"));
		locales.put("sr_BA(*)", new Locale("Serbian (Cyrillic)", "Bosnia and Herzegovina", "sr_BA(*)"));
		locales.put("sr_ME(*)", new Locale("Serbian (Cyrillic)", "Montenegro", "sr_ME(*)"));
		locales.put("sr_RS(*)", new Locale("Serbian (Cyrillic)", "Serbia", "sr_RS(*)"));
		locales.put("sr_Latn_BA(**)", new Locale("Serbian (Latin)", "Bosnia and Herzegovina", "sr_Latn_BA(**)"));
		locales.put("sr_Latn_ME(**)", new Locale("Serbian (Latin)", "Montenegro", "sr_Latn_ME(**)"));
		locales.put("sr_Latn_RS(**)", new Locale("Serbian (Latin)", "Serbia", "sr_Latn_RS(**)"));
		locales.put("sk_SK", new Locale("Slovak", "Slovakia", "sk_SK"));
		locales.put("sl_SI", new Locale("Slovenian", "Slovenia", "sl_SI"));
		locales.put("es_AR", new Locale("Spanish", "Argentina", "es_AR"));
		locales.put("es_BO", new Locale("Spanish", "Bolivia", "es_BO"));
		locales.put("es_CL", new Locale("Spanish", "Chile", "es_CL"));
		locales.put("es_CO", new Locale("Spanish", "Colombia", "es_CO"));
		locales.put("es_CR", new Locale("Spanish", "Costa Rica", "es_CR"));
		locales.put("es_DO", new Locale("Spanish", "Dominican Republic", "es_DO"));
		locales.put("es_EC", new Locale("Spanish", "Ecuador", "es_EC"));
		locales.put("es_SV", new Locale("Spanish", "El Salvador", "es_SV"));
		locales.put("es_GT", new Locale("Spanish", "Guatemala", "es_GT"));
		locales.put("es_HN", new Locale("Spanish", "Honduras", "es_HN"));
		locales.put("es_MX", new Locale("Spanish", "Mexico", "es_MX"));
		locales.put("es_NI", new Locale("Spanish", "Nicaragua", "es_NI"));
		locales.put("es_PA", new Locale("Spanish", "Panama", "es_PA"));
		locales.put("es_PY", new Locale("Spanish", "Paraguay", "es_PY"));
		locales.put("es_PE", new Locale("Spanish", "Peru", "es_PE"));
		locales.put("es_PR", new Locale("Spanish", "Puerto Rico", "es_PR"));
		locales.put("es_ES", new Locale("Spanish", "Spain", "es_ES"));
		locales.put("es_US(*)", new Locale("Spanish", "United States", "es_US(*)"));
		locales.put("es_UY", new Locale("Spanish", "Uruguay", "es_UY"));
		locales.put("es_VE", new Locale("Spanish", "Venezuela", "es_VE"));
		locales.put("sv_SE", new Locale("Swedish", "Sweden", "sv_SE"));
		locales.put("th_TH", new Locale("Thai (Western digits)", "Thailand", "th_TH"));
		locales.put("th_TH_TH", new Locale("Thai (Thai digits)", "Thailand", "th_TH_TH"));
		locales.put("tr_TR", new Locale("Turkish", "Turkey", "tr_TR"));
		locales.put("uk_UA", new Locale("Ukrainian", "Ukraine", "uk_UA"));
		locales.put("vi_VN", new Locale("Vietnamese", "Vietnam", "vi_VN"));
	}
	/**
	 * Return if the locales contain the specific key
	 * @param str
	 * @return
	 */
	public static boolean isLocale(String str){
		return locales.containsKey(str);
	}
	
	/**
	 * Return the specific locales
	 * @param str
	 * @return
	 */
	public static Locale getLocale(String str){
		if (isLocale(str)){
			return locales.get(str);
		}
		return null;
	}
}