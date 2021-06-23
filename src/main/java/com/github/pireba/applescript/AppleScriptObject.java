package com.github.pireba.applescript;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An AppleScriptObject represents an object in AppleScript.
 * Such an object can have one of the following classes:
 * <ul>
 * <li>Alias</li>
 * <li>Boolean</li>
 * <li>Date</li>
 * <li>Double</li>
 * <li>File</li>
 * <li>Integer</li>
 * <li>{@linkplain AppleScriptList}</li>
 * <li>{@linkplain AppleScriptMap}</li>
 * <li>String</li>
 * </ul>
 * Each value is internal stored as a string.
 * The getter methods check the values against regular expressions and try to convert them to the desired class.
 * If you request an invalid class or the conversion does not work, an {@linkplain AppleScriptException} is thrown.<br>
 * <br>
 * Example calls how to retrieve data from an {@code AppleScriptObject}.<br>
 * <br>
 * <b>Alias</b><br>
 * {@code object.getAlias();} =&gt; "Macintosh HD:Users:phillip:"<br>
 * <br>
 * <b>Boolean</b><br>
 * {@code object.getBoolean();} =&gt; true<br>
 * {@code object.getBoolean();} =&gt; false<br>
 * <br>
 * <b>Date</b><br>
 * {@code object.getDate(AppleScriptDateFormat.ENGLISH);} =&gt; "Sun Feb 07 17:17:31 CET 2021"<br>
 * <br>
 * <b>Double</b><br>
 * {@code object.getDouble();} =&gt; 123.456<br>
 * <br>
 * <b>File</b><br>
 * {@code object.getFile();} =&gt; "Macintosh HD:Users:phillip:foo.txt"<br>
 * <br>
 * <b>Integer</b><br>
 * {@code object.getInteger();} =&gt; 123<br>
 * <br>
 * <b>{@linkplain AppleScriptList}</b><br>
 * {@code AppleScriptList list = object.getList();}<br>
 * {@code list.getString(0);} =&gt; "foo"<br>
 * <br>
 * <b>{@linkplain AppleScriptMap}</b><br>
 * {@code AppleScriptMap map = object.getAppleScriptMap();}<br>
 * {@code map.getString("foo");} =&gt; "bar"<br>
 * <br>
 * <b>String</b><br>
 * {@code object.getString();} =&gt; "foo"<br>
 */
public class AppleScriptObject {
	
	/**
	 * Regular expression that matches an alias value.
	 */
	private static final String REGEX_ALIAS = "^alias\\ \\\"(.*)\\\"$";
	
	/**
	 * Regular expression that matches a boolean value.
	 */
	private static final String REGEX_BOOLEAN = "(?i)(true|false)";
	
	/**
	 * Regular expression that matches a date value.
	 */
	private static final String REGEX_DATE = "^date\\ \\\"(.*)\\\"$";
	
	/**
	 * Regular expression that matches a double value.
	 */
	private static final String REGEX_DOUBLE = "^[-+]?[0-9]*\\.[0-9]+([eE][-+]?[0-9]+)?$";
	
	/**
	 * Regular expression that matches a file value.
	 */
	private static final String REGEX_FILE = "^file\\ \\\"(.*)\\\"$";
	
	/**
	 * Regular expression that matches an integer value.
	 */
	private static final String REGEX_INTEGER = "^\\-?[0-9]+$";
	
	/**
	 * Regular expression that matches a string value.
	 */
	private static final String REGEX_STRING = "^\\\"(.*)\\\"$";
	
	/**
	 * The object to store the value.
	 */
	private Object object;
		
	/**
	 * Creates a new AppleScriptObject from a string.
	 * 
	 * @param value
	 * 		The string value.
	 */
	public AppleScriptObject(String value) {
		this.object = value;
	}
	
	/**
	 * Creates a new {@code AppleScriptObject} from an {@linkplain AppleScriptTokenizer}.
	 * 
	 * @param tokener
	 * 		The {@linkplain AppleScriptTokenizer}.
	 * @throws AppleScriptException
	 * 		Thrown if there is a syntax error in the source string.
	 */
	public AppleScriptObject(AppleScriptTokenizer tokener) throws AppleScriptException {
		String value = tokener.getNextValue();
		
		if ( value.equals("{") ) {
			tokener.goBack();
			this.object = new AppleScriptList(tokener);
			return;
		} else {
			this.object = value;
		}
	}
	
	/**
	 * Creates a new {@code AppleScriptObject} from the given {@linkplain AppleScriptList}.
	 * This constructor checks whether the list is actually an {@linkplain AppleScriptMap} and converts it if necessary.
	 * 
	 * @param list
	 * 		The {@linkplain AppleScriptList}.
	 * @throws AppleScriptException
	 * 		If an error occurs while converting to a map.
	 */
	public AppleScriptObject(AppleScriptList list) throws AppleScriptException {
		if ( AppleScriptMap.isListActuallyAMap(list) ) {
			this.object = new AppleScriptMap(list);
		} else {
			this.object = list;
		}
	}
	
	/**
	 * Creates a new {@code AppleScriptObject} from the given {@linkplain AppleScriptMap}.
	 * 
	 * @param map
	 * 		The {@linkplain AppleScriptMap}.
	 */
	public AppleScriptObject(AppleScriptMap map) {
		this.object = map;
	}
	
	/**
	 * Gets the alias value of this object.
	 * The return value is a HFS path string.
	 * You have to do the conversion to a POSIX path yourself.
	 * 
	 * @return
	 * 		The alias value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not an alias.
	 */
	public String getAlias() throws AppleScriptException {
		try {
			String string = (String) this.object;
			return string.replaceAll(REGEX_ALIAS, "$1");
		} catch ( Exception e ) {
			throw new AppleScriptException("Object is not an Alias.", e);
		}
	}
	
	/**
	 * Gets the boolean value of this object.
	 * 
	 * @return
	 * 		The boolean value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not a boolean.
	 */
	public boolean getBoolean() throws AppleScriptException {
		try {
			String string = (String) this.object;
			return Boolean.parseBoolean(string);
		} catch ( Exception e ) {
			throw new AppleScriptException("Object is not a Boolean.", e);
		}
	}
	
	/**
	 * Gets the date value of this object.
	 * 
	 * @param dateFormat
	 * 		The date format as an {@linkplain AppleScriptDateFormat}.
	 * @return
	 * 		The date value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not a date.
	 */
	public Date getDate(AppleScriptDateFormat dateFormat) throws AppleScriptException {
		return this.getDate(dateFormat.getFormat());
	}
	
	/**
	 * Gets the date value of this object.
	 * 
	 * @param dateFormat
	 * 		The date format as a {@linkplain SimpleDateFormat}.
	 * @return
	 * 		The date value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not a date.
	 */
	public Date getDate(SimpleDateFormat dateFormat) throws AppleScriptException {
		try {
			String string = (String) this.object;
			String datePart = string.replaceAll(REGEX_DATE, "$1");
			return dateFormat.parse(datePart);
		} catch ( Exception e ) {
			throw new AppleScriptException("Object is not a Date.", e);
		}
	}
	
	/**
	 * Gets the double value of this object.
	 * 
	 * @return
	 * 		The double value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not a double.
	 */
	public double getDouble() throws AppleScriptException {
		try {
			String string = (String) this.object;
			return Double.parseDouble(string);
		} catch ( Exception e ) {
			throw new AppleScriptException("Object is not a Double.", e);
		}
	}
	
	/**
	 * Gets the file value of this object.
	 * The return value is a HFS path string.
	 * You have to do the conversion to a POSIX path yourself.
	 * 
	 * @return
	 * 		The file value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not a file.
	 */
	public String getFile() throws AppleScriptException {
		try {
			String string = (String) this.object;
			return string.replaceAll(REGEX_FILE, "$1");
		} catch ( Exception e ) {
			throw new AppleScriptException("Object is not a File.", e);
		}
	}
	
	/**
	 * Gets the integer value of this object.
	 * 
	 * @return
	 * 		The integer value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not an integer.
	 */
	public int getInteger() throws AppleScriptException {
		try {
			String string = (String) this.object;
			return Integer.parseInt(string);
		} catch ( Exception e ) {
			throw new AppleScriptException("Object is not an Integer.", e);
		}
	}
	
	/**
	 * Gets the {@linkplain AppleScriptList} value of this object.
	 * 
	 * @return
	 * 		The {@linkplain AppleScriptList} value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not an {@linkplain AppleScriptList}.
	 */
	public AppleScriptList getList() throws AppleScriptException {
		if ( this.isList() ) {
			return (AppleScriptList) this.object;
		} else {
			throw new AppleScriptException("Object is not an AppleScriptList.");
		}
	}
	
	/**
	 * Gets the {@linkplain AppleScriptMap} value of this object.
	 * 
	 * @return
	 * 		The {@linkplain AppleScriptMap} value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not an {@linkplain AppleScriptMap}.
	 */
	public AppleScriptMap getMap() throws AppleScriptException {
		if ( this.isMap() ) {
			return (AppleScriptMap) this.object;
		} else {
			throw new AppleScriptException("Object is not an AppleScriptMap.");
		}
	}
	
	/**
	 * Gets the object value of this object.
	 * 
	 * @return
	 * 		The object value.
	 */
	public Object getObject() {
		return this.object;
	}
	
	/**
	 * Gets the string value of this object.
	 * 
	 * @return
	 * 		The string value.
	 * @throws AppleScriptException
	 * 		Is thrown if this object is not a string.
	 */
	public String getString() throws AppleScriptException {
		try {
			String string = (String) this.object;
			return string.replaceAll(REGEX_STRING, "$1");
		} catch ( Exception e ) {
			throw new AppleScriptException("Object is not a String.", e);
		}
	}
	
	/**
	 * Checks if this object is an alias.
	 * 
	 * @return
	 * 		{@code True} if this object is an alias - {@code False} otherwise.
	 */
	public boolean isAlias() {
		try {
			String string = (String) this.object;
			if ( string.matches(REGEX_ALIAS) ) {
				return true;
			} else {
				return false;
			}
		} catch ( Exception e ) {
			return false;
		}
	}
	
	/**
	 * Checks if this object is a boolean.
	 * 
	 * @return
	 * 		{@code True} if this object is a boolean - {@code False} otherwise.
	 */
	public boolean isBoolean() {
		try {
			String string = (String) this.object;
			if ( string.matches(REGEX_BOOLEAN) ) {
				return true;
			} else {
				return false;
			}
		} catch ( Exception e ) {
			return false;
		}
	}
	
	/**
	 * Checks if this object is a Date.
	 * 
	 * @return
	 * 		{@code True} if this object is a Date - {@code False} otherwise.
	 */
	public boolean isDate() {
		try {
			String string = (String) this.object;
			if ( string.matches(REGEX_DATE) ) {
				return true;
			} else {
				return false;
			}
		} catch ( Exception e ) {
			return false;
		}
	}
	
	/**
	 * Checks if this object is a double.
	 * 
	 * @return
	 * 		{@code True} if this object is a double - {@code False} otherwise.
	 */
	public boolean isDouble() {
		try {
			String string = (String) this.object;
			if ( string.matches(REGEX_DOUBLE) ) {
				return true;
			} else {
				return false;
			}
		} catch ( Exception e ) {
			return false;
		}
	}
	
	/**
	 * Checks if this object is a file.
	 * 
	 * @return
	 * 		{@code True} if this object is a file - {@code False} otherwise.
	 */
	public boolean isFile() {
		try {
			String string = (String) this.object;
			if ( string.matches(REGEX_FILE) ) {
				return true;
			} else {
				return false;
			}
		} catch ( Exception e ) {
			return false;
		}
	}
	
	/**
	 * Checks if this object is an integer.
	 * 
	 * @return
	 * 		{@code True} if this object is an integer - {@code False} otherwise.
	 */
	public boolean isInteger() {
		try {
			String string = (String) this.object;
			if ( string.matches(REGEX_INTEGER) ) {
				return true;
			} else {
				return false;
			}
		} catch ( Exception e ) {
			return false;
		}
	}
	
	/**
	 * Checks if this object is an {@linkplain AppleScriptList}.
	 * 
	 * @return
	 * 		{@code True} if this object is a {@linkplain AppleScriptList} - {@code False} otherwise.
	 */
	public boolean isList() {
		return this.object instanceof AppleScriptList;
	}
	
	/**
	 * Checks if this object is an {@linkplain AppleScriptMap}.
	 * 
	 * @return
	 * 		{@code True} if this object is an {@linkplain AppleScriptMap} - {@code False} otherwise.
	 */
	public boolean isMap() {
		return this.object instanceof AppleScriptMap;
	}
	
	/**
	 * Checks if this object is a string.
	 * 
	 * @return
	 * 		{@code True} if this object is a string - {@code False} otherwise.
	 */
	public boolean isString() {
		try {
			if ( this.object.toString().matches(REGEX_STRING) ) {
				return true;
			} else if ( this.object instanceof String ) {
				return true;
			} else {
				return false;
			}
		} catch ( Exception e ) {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return this.object.toString();
	}
}