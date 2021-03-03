package com.github.pireba.applescript;


import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Enum for a collection of standard date formats of different system languages.
 * A date in AppleScript always has the format that is set in the language settings of the operating system.
 * This is a collection of some languages and their associated standard date formats.
 */
public enum AppleScriptDateFormat {
	
	/**
	 * Default date format for Chinese language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * 2021年2月7日 星期日 17:17:31
	 * 
	 */
	CHINESE(new SimpleDateFormat("y年M月d日 EEEE hh:mm:ss", Locale.CHINESE)),
	
	/**
	 * Default date format for English language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * Sunday, 7. February 2021 at 17:17:31
	 */
	ENGLISH(new SimpleDateFormat("EEEE, d. MMMM y 'at' hh:mm:ss", Locale.ENGLISH)),
	
	/**
	 * Default date format for French language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * dimanche 7 février 2021 à 17:17:31
	 */
	FRENCH(new SimpleDateFormat("EEEE d MMMM y 'à' hh:mm:ss", Locale.FRENCH)),
	
	/**
	 * Default date format for German language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * Sonntag, 7. Februar 2021 um 17:17:31
	 */
	GERMAN(new SimpleDateFormat("EEEE, d. MMMM y 'um' hh:mm:ss", Locale.GERMAN)),
	
	/**
	 * Default date format for Greek language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * Κυριακή, 7 Φεβρουαρίου 2021 - 17:17:31
	 */
	GREEK(new SimpleDateFormat("EEEE, d MMMM y '-' hh:mm:ss", new Locale("el", "GR"))),
	
	/**
	 * Default date format for Italian language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * domenica 7 febbraio 2021 17:17:31
	 */
	ITALIAN(new SimpleDateFormat("EEEE d MMMM y hh:mm:ss", Locale.ITALIAN)),
	
	/**
	 * Default date format for Japanese language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * 2021年2月7日 日曜日 17:17:31
	 */
	JAPANESE(new SimpleDateFormat("y年M月d日 EEEE hh:mm:ss", Locale.JAPANESE)),
	
	/**
	 * Default date format for Korean language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * 2021년 2월 7일 일요일 17:17:31
	 */
	KOREAN(new SimpleDateFormat("y년M월d일 EEEE hh:mm:ss", Locale.KOREAN)),
	
	/**
	 * Default date format for Dutch language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * zondag 7 februari 2021 om 17:17:31
	 */
	DUTCH(new SimpleDateFormat("EEEE d MMMM y 'om' hh:mm:ss", new Locale("nl", "NL"))),
		
	/**
	 * Default date format for Polish language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * niedziela, 7 lutego 2021 17:17:31
	 */
	POLISH(new SimpleDateFormat("EEEE, d MMMM y hh:mm:ss", new Locale("pl", "PL"))),
	
	/**
	 * Default date format for Portuguese language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * domingo, 7 de fevereiro de 2021 17:17:31
	 */
	PORTUGUESE(new SimpleDateFormat("EEEE, d 'de' MMMM 'de' y hh:mm:ss", new Locale("pt", "PT"))),
	
	/**
	 * Default date format for Russian language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * воскресенье, 7 февраля 2021 г. в 17:17:31
	 */
	RUSSIAN(new SimpleDateFormat("EEEE, d MMMM y 'г. в' hh:mm:ss", new Locale("ru", "RU"))),
	
	/**
	 * Default date format for Spanish language settings.<br>
	 * <br>
	 * Example Date:<br>
	 * domingo, 7 de febrero de 2021, 17:17:31
	 */
	SPANISH(new SimpleDateFormat("EEEE, d 'de' MMMM 'de' y, hh:mm:ss", new Locale("es", "ES"))),
	;
	
	/**
	 * The {@linkplain SimpleDateFormat}.
	 */
	private final SimpleDateFormat format;
	
	/**
	 * Creates a new {@code AppleScriptDateFormat} with a specified {@linkplain SimpleDateFormat}.
	 * 
	 * @param format
	 * 		A {@linkplain SimpleDateFormat}.
	 */
	private AppleScriptDateFormat(SimpleDateFormat format) {
		this.format = format;
	}
	
	/**
	 * Gets the {@linkplain SimpleDateFormat}.
	 * 
	 * @return
	 * 		The {@linkplain SimpleDateFormat}.
	 */
	public SimpleDateFormat getFormat() {
		return this.format;
	}
}