package com.github.pireba.applescript;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * An {@code AppleScriptList} is an ordered sequence of {@linkplain AppleScriptObject} values.
 * It represents the list class in AppleScript.
 * The internal form is an ArrayList, so the usual List methods are available.
 */
public class AppleScriptList extends ArrayList<AppleScriptObject> {
	
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new {@code AppleScriptList} from an {@linkplain AppleScriptTokenizer}.
	 * 
	 * @param tokener
	 * 		An {@linkplain AppleScriptTokenizer}.
	 * @throws AppleScriptException
	 * 		Thrown if there is a syntax error in the source string.
	 */
	public AppleScriptList(AppleScriptTokenizer tokener) throws AppleScriptException {
		String value = tokener.getNextValue();
		
		if ( ! value.equals("{") ) {
			throw new AppleScriptException("A List must begin with a curly bracket ({): '"+value+"'.");
		}
		
		while ( tokener.hasMore() ) {
			value = tokener.getNextValue();
			
			switch (value) {
			case "{":
				tokener.goBack();
				AppleScriptList list = new AppleScriptList(tokener);
				AppleScriptObject object = new AppleScriptObject(list);
				this.add(object);
				break;
			case "}":
				return;
			case ",":
				continue;
			default:
				this.add(new AppleScriptObject(value));
			}
		}
	}
	
	/**
	 * Gets the alias value for the given index.
	 * The return value is a HFS path string.
	 * You have to do the conversion to a POSIX path yourself.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		The alias value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not an alias.
	 */
	public String getAlias(int index) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getAlias();
	}
	
	/**
	 * Gets the boolean value for the given index.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		The boolean value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a boolean.
	 */
	public boolean getBoolean(int index) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getBoolean();
	}
	
	/**
	 * Gets the date value for the given index.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @param dateFormat
	 * 		The date format as an {@linkplain AppleScriptDateFormat}.
	 * @return
	 * 		The date value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a date.
	 */
	public Date getDate(int index, AppleScriptDateFormat dateFormat) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getDate(dateFormat);
	}
	
	/**
	 * Gets the date value for the given index.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @param dateFormat
	 * 		The date format as a {@linkplain SimpleDateFormat}.
	 * @return
	 * 		The date value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a date.
	 */
	public Date getDate(int index, SimpleDateFormat dateFormat) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getDate(dateFormat);
	}
	
	/**
	 * Gets the double value for the given index.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		The double value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a double.
	 */
	public double getDouble(int index) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getDouble();
	}
	
	/**
	 * Gets the file value for the given index.
	 * The return value is a HFS path string.
	 * You have to do the conversion to a POSIX path yourself.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		The file value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a file.
	 */
	public String getFile(int index) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getFile();
	}
	
	/**
	 * Gets the integer value for the given index.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		The integer value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not an integer.
	 */
	public int getInteger(int index) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getInteger();
	}
	
	/**
	 * Gets the {@linkplain AppleScriptList} value for the given index.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		The {@linkplain AppleScriptList} value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not an {@linkplain AppleScriptList}.
	 */
	public AppleScriptList getList(int index) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getList();
	}
	
	/**
	 * Gets the {@linkplain AppleScriptMap} value for the given index.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		The {@linkplain AppleScriptMap} value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not an {@linkplain AppleScriptMap}.
	 */
	public AppleScriptMap getMap(int index) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getMap();
	}
	
	/**
	 * Gets the string value for the given index.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		The string value.
	 * @throws AppleScriptException
	 * 		Is thrown if the item is not a string.
	 */
	public String getString(int index) throws AppleScriptException {
		AppleScriptObject object = this.get(index);
		return object.getString();
	}
	
	/**
	 * Checks if the item with the given index is an alias.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		{@code True} if the item is an alias - {@code False} otherwise.
	 */
	public boolean isAlias(int index) {
		AppleScriptObject object = this.get(index);
		return object.isAlias();
	}
	
	/**
	 * Checks if the item with the given index is a boolean.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		{@code True} if the item is a boolean - {@code False} otherwise.
	 */
	public boolean isBoolean(int index) {
		AppleScriptObject object = this.get(index);
		return object.isBoolean();
	}
	
	/**
	 * Checks if the item with the given index is a Date.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		{@code True} if the item is a Date - {@code False} otherwise.
	 */
	public boolean isDate(int index) {
		AppleScriptObject object = this.get(index);
		return object.isDate();
	}
	
	/**
	 * Checks if the item with the given index is a double.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		{@code True} if the item is a double - {@code False} otherwise.
	 */
	public boolean isDouble(int index) {
		AppleScriptObject object = this.get(index);
		return object.isDouble();
	}
	
	/**
	 * Checks if the item with the given index is a file.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		{@code True} if the item is a file - {@code False} otherwise.
	 */
	public boolean isFile(int index) {
		AppleScriptObject object = this.get(index);
		return object.isFile();
	}
	
	/**
	 * Checks if the item with the given index is an integer.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		{@code True} if the item is an integer - {@code False} otherwise.
	 */
	public boolean isInteger(int index) {
		AppleScriptObject object = this.get(index);
		return object.isInteger();
	}
	
	/**
	 * Checks if the item with the given index is an {@linkplain AppleScriptList}.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		{@code True} if the item is a {@linkplain AppleScriptList} - {@code False} otherwise.
	 */
	public boolean isList(int index) {
		AppleScriptObject object = this.get(index);
		return object.isList();
	}
	
	/**
	 * Checks if the item with the given index is an {@linkplain AppleScriptMap}.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		{@code True} if the item is an {@linkplain AppleScriptMap} - {@code False} otherwise.
	 */
	public boolean isMap(int index) {
		AppleScriptObject object = this.get(index);
		return object.isMap();
	}
	
	/**
	 * Checks if the item with the given index is a string.
	 * 
	 * @param index
	 * 		The index of the item.
	 * @return
	 * 		{@code True} if the item is a string - {@code False} otherwise.
	 */
	public boolean isString(int index) {
		AppleScriptObject object = this.get(index);
		return object.isString();
	}
}