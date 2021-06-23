package com.github.pireba.applescript;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * An {@code AppleScriptMap} represents the record class in AppleScript.
 * The map contains pairs of string keys and {@linkplain AppleScriptObject} values.
 */
public class AppleScriptMap extends HashMap<String, AppleScriptObject>{
	
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Regular expression that matches a key-value pair.
	 */
	private static final String REGEX_MAP = "^([A-Za-z][A-Za-z0-9_\\ ]+|\\|.*\\|)\\:(.*)$";
	
	/**
	 * Creates an {@linkplain AppleScriptMap} from an {@linkplain AppleScriptList}.
	 * A list must consist of key-value strings.
	 * Each list item is converted to a string key and an {@linkplain AppleScriptObject} value.
	 * 
	 * @param list
	 * 		The {@linkplain AppleScriptList}.
	 * @throws AppleScriptException
	 * 		If a list item is not a convertible string.
	 */
	public AppleScriptMap(AppleScriptList list) throws AppleScriptException {
		for ( AppleScriptObject object : list ) {
			String string = object.toString();
			String key = this.getKey(string);
			AppleScriptObject value = this.getValue(string);
			this.put(key, value);
		}
	}
	
	/**
	 * Checks if the list is actually a map.
	 * In AppleScript, a map looks like a list.
	 * The only difference is that the list items are always key-value pairs.
	 * This function checks against a regular expression whether the elements of the list are key-value pairs.
	 * 
	 * @param list
	 * 		The {@linkplain AppleScriptList} to check.
	 * @return
	 * 		True if the List is a Map - false otherwise.
	 * @throws AppleScriptException
	 * 		If the first element of the {@linkplain AppleScriptList} cannot be retrieved for checking.
	 */
	public static boolean isListActuallyAMap(AppleScriptList list) throws AppleScriptException {
		if ( list == null ) {
			return false;
		}
		
		if ( list.size() <= 0 ) {
			return false;
		}
		
		AppleScriptObject object = list.get(0);
		
		if ( ! object.toString().matches(REGEX_MAP) ) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the key part of a key-value string.
	 * 
	 * @param string
	 * 		The key-value string.
	 * @return
	 * 		The key part of the key-value string.
	 */
	private String getKey(String string) {
		return string.replaceAll(REGEX_MAP, "$1");
	}
	
	/**
	 * Gets the value part of a key-value string as an {@linkplain AppleScriptObject}.
	 * 
	 * @param string
	 * 		The key-value string.
	 * @return
	 * 		The value part of the key-value string as an {@linkplain AppleScriptObject}.
	 * @throws AppleScriptException
	 * 		If an error occurs when creating an AppleScriptObject from the value part.
	 */
	private AppleScriptObject getValue(String string) throws AppleScriptException {
		String value = string.replaceAll(REGEX_MAP, "$2");
		return new AppleScriptObject(value);
	}
	
	/**
	 * Gets the alias value for the given key.
	 * The return value is a HFS path string.
	 * You have to do the conversion to a POSIX path yourself.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		The alias value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not an alias.
	 */
	public String getAlias(String key) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getAlias();
	}
	
	/**
	 * Gets the boolean value for the given key.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		The boolean value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a boolean.
	 */
	public boolean getBoolean(String key) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getBoolean();
	}
	
	/**
	 * Gets the date value for the given key.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @param dateFormat
	 * 		The date format as an {@linkplain AppleScriptDateFormat}.
	 * @return
	 * 		The date value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a date.
	 */
	public Date getDate(String key, AppleScriptDateFormat dateFormat) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getDate(dateFormat);
	}
	
	/**
	 * Gets the date value for the given key.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @param dateFormat
	 * 		The date format as a {@linkplain SimpleDateFormat}.
	 * @return
	 * 		The date value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a date.
	 */
	public Date getDate(String key, SimpleDateFormat dateFormat) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getDate(dateFormat);
	}
	
	/**
	 * Gets the double value for the given key.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		The double value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a double.
	 */
	public double getDouble(String key) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getDouble();
	}
	
	/**
	 * Gets the file value for the given key.
	 * The return value is a HFS path string.
	 * You have to do the conversion to a POSIX path yourself.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		The file value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a file.
	 */
	public String getFile(String key) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getFile();
	}
	
	/**
	 * Gets the integer value for the given key.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		The integer value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not an integer.
	 */
	public int getInteger(String key) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getInteger();
	}
	
	/**
	 * Gets the {@linkplain AppleScriptList} value for the given key.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		The {@linkplain AppleScriptList} value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not an {@linkplain AppleScriptList}.
	 */
	public AppleScriptList getList(String key) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getList();
	}
	
	/**
	 * Gets the {@linkplain AppleScriptMap} value for the given key.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		The {@linkplain AppleScriptMap} value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not an {@linkplain AppleScriptMap}.
	 */
	public AppleScriptMap getMap(String key) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getMap();
	}
	
	/**
	 * Gets the string value for the given key.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		The string value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a string.
	 */
	public String getString(String key) throws AppleScriptException {
		AppleScriptObject object = this.get(key);
		return object.getString();
	}
	
	/**
	 * Checks if the item with the given key is an alias.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		{@code True} if the item is an alias - {@code False} otherwise.
	 */
	public boolean isAlias(String key) {
		AppleScriptObject object = this.get(key);
		return object.isAlias();
	}
	
	/**
	 * Checks if the item with the given key is a boolean.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		{@code True} if the item is a boolean - {@code False} otherwise.
	 */
	public boolean isBoolean(String key) {
		AppleScriptObject object = this.get(key);
		return object.isBoolean();
	}
	
	/**
	 * Checks if the item with the given key is a Date.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		{@code True} if the item is a Date - {@code False} otherwise.
	 */
	public boolean isDate(String key) {
		AppleScriptObject object = this.get(key);
		return object.isDate();
	}
	
	/**
	 * Checks if the item with the given key is a double.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		{@code True} if the item is a double - {@code False} otherwise.
	 */
	public boolean isDouble(String key) {
		AppleScriptObject object = this.get(key);
		return object.isDouble();
	}
	
	/**
	 * Checks if the item with the given key is a file.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		{@code True} if the item is a file - {@code False} otherwise.
	 */
	public boolean isFile(String key) {
		AppleScriptObject object = this.get(key);
		return object.isFile();
	}
	
	/**
	 * Checks if the item with the given key is an integer.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		{@code True} if the item is an integer - {@code False} otherwise.
	 */
	public boolean isInteger(String key) {
		AppleScriptObject object = this.get(key);
		return object.isInteger();
	}
	
	/**
	 * Checks if the item with the given key is an {@linkplain AppleScriptList}.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		{@code True} if the item is a {@linkplain AppleScriptList} - {@code False} otherwise.
	 */
	public boolean isList(String key) {
		AppleScriptObject object = this.get(key);
		return object.isList();
	}
	
	/**
	 * Checks if the item with the given key is an {@linkplain AppleScriptMap}.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		{@code True} if the item is an {@linkplain AppleScriptMap} - {@code False} otherwise.
	 */
	public boolean isMap(String key) {
		AppleScriptObject object = this.get(key);
		return object.isMap();
	}
	
	/**
	 * Checks if the item with the given key is a string.
	 * 
	 * @param key
	 * 		The key of the item.
	 * @return
	 * 		{@code True} if the item is a string - {@code False} otherwise.
	 */
	public boolean isString(String key) {
		AppleScriptObject object = this.get(key);
		return object.isString();
	}
}