package com.github.pireba.applescript;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * An AppleScriptTokenizer takes a source string and extracts characters and tokens from it.
 * It is used by the {@linkplain AppleScriptObject} and {@linkplain AppleScriptList} constructors to parse the result of an {@linkplain AppleScript} execution.
 */
public class AppleScriptTokenizer {
	
	/**
	 * The previous character read from the input.
	 */
	private char previous = 0;
	
	/**
	 * The Reader for the input.
	 */
	private final Reader reader;
	
	/**
	 * Indicates whether the last character should be retrieved.
	 */
	private boolean usePrevious = false;
	
	/**
	 * Construct an AppleScriptTokenizer from the given string.
	 * 
	 * @param input
	 * 		The string.
	 */
	public AppleScriptTokenizer(String input) {
		this(new StringReader(input));
	}
	
	/**
	 * Construct an AppleScriptTokenizer from the given InputStream.<br>
	 * The caller must close the InputStream.
	 * 
	 * @param input
	 * 		The InputStream.
	 */
	public AppleScriptTokenizer(InputStream input) {
		this(new InputStreamReader(input));
	}
	
	/**
	 * Construct an AppleScriptTokenizer from the given Reader.<br>
	 * The caller must close the Reader.
	 * 
	 * @param input
	 * 		The Reader.
	 */
	public AppleScriptTokenizer(Reader input) {
		if ( input.markSupported() ) {
			this.reader = input;
		} else {
			this.reader = new BufferedReader(input);
		}
	}
	
	/**
	 * Get the next character from the Reader.
	 * 
	 * @return
	 * 		The next character - or '0' if the end of the Reader has been reached.
	 * @throws AppleScriptException
	 * 		Thrown if there is an error getting the next character.
	 */
	private char getNextChar() throws AppleScriptException {
		if ( this.usePrevious ) {
			this.usePrevious = false;
			return this.previous;
		}
		
		try {
			int c = this.reader.read();
			
			if ( c <= 0 ) {
				return 0;
			} else {
				this.previous = (char) c;
				return this.previous;
			}
		} catch (IOException e) {
			throw new AppleScriptException("Error while getting next char.", e);
		}
	}
	
	/**
	 * Get the next string value.
	 * A string must begin and end with double quotes ("foo").
	 * 
	 * @return
	 * 		The string between the double quotes.
	 * @throws AppleScriptException
	 * 		Thrown if the string does not start or end with double quotes.
	 */
	private String getNextString() throws AppleScriptException {
		StringBuilder sb = new StringBuilder();
		
		char c = this.getNextChar();
		if ( c != '"' ) {
			throw new AppleScriptException("A string must begin with a double quote (\"): '"+c+"'.");
		}
		
		sb.append(c);
		while ( (c = this.getNextChar()) != 0 ) {
			switch (c) {
			case '\\':
				c = this.getNextChar();
				switch (c) {
				case 0:
					throw new AppleScriptException("Unexpected end of file.");
				case '\n':
					sb.append('\n');
					break;
				case '\r':
					sb.append('\r');
					break;
				case '\t':
					sb.append('\t');
					break;
				case '\b':
					sb.append('\b');
					break;
				case '\f':
					sb.append('\f');
					break;
				case '"':
				case '\'':
				case '\\':
				case '/':
					sb.append(c);
					break;
				default:
					throw new AppleScriptException("Illegal escape sequence.");
				}
				break;
			case '"':
				sb.append(c);
				return sb.toString();
			default:
				sb.append(c);
				break;
			}
		}
		
		throw new AppleScriptException("Unexpected end of file.");
	}
	
	/**
	 * Get the next value as a string.<br>
	 * The value can be one of the possible AppleScript classes:
	 * <ul>
	 *  <li>Alias</li>
	 *  <li>Boolean</li>
	 *  <li>Date</li>
	 *  <li>Double</li>
	 *  <li>File</li>
	 *  <li>Integer</li>
	 *  <li>String</li>
	 * </ul>
	 * 
	 * It may also be one of the following special characters:
	 * <ul>
	 *  <li>{ =&gt; Indicates the start of an {@linkplain AppleScriptList}.</li>
	 *  <li>} =&gt; Indicates the end of an {@linkplain AppleScriptList}.</li>
	 *  <li>, =&gt; Indicates that another list item follows.</li>
	 * </ul>
	 * <br>
	 * 
	 * @return
	 * 		The next value.
	 * @throws
	 * 		AppleScriptException Thrown when a syntax error occurs.
	 * @see
	 * 		<a
	 * 			href="https://developer.apple.com/library/archive/documentation/AppleScript/Conceptual/AppleScriptLangGuide/reference/ASLR_classes.html">
	 * 			AppleScript Language Guide
	 * 		</a>
	 */
	public String getNextValue() throws AppleScriptException {
		char c = this.getNextChar();
		
		switch (c) {
		case 0:
			throw new AppleScriptException("Unexpected end of file.");
		case '{':
		case '}':
			return String.valueOf(c);
		case ',':
			// Skip one character. After a comma always comes a space.
			this.getNextChar();
			return String.valueOf(c);
		case '"':
			this.goBack();
			return this.getNextString();
		}
		
		this.goBack();
		
		StringBuilder sb = new StringBuilder();
		loop: while ( (c = this.getNextChar()) != 0 ) {
			switch (c) {
			case '{':
			case '}':
			case ',':
				this.goBack();
				break loop;
			case '"':
				this.goBack();
				sb.append(this.getNextString());
				break;
			default:
				sb.append(c);
				break;
			}
		}
		
		return sb.toString().trim();
	}
	
	/**
	 * Go back one character.
	 * This provides a preview function for one character.
	 * This allows you to test for the next character before attempting to parse the next number or value.
	 * 
	 * @throws AppleScriptException
	 * 		Thrown if trying to go back more than one step.
	 */
	public void goBack() throws AppleScriptException {
		if ( this.usePrevious ) {
			throw new AppleScriptException("You can only go one step back.");
		}
		
		this.usePrevious = true;
	}
	
	/**
	 * Checks if more characters can be read from the Reader.
	 * 
	 * @return
	 * 		True if more characters can be read - false otherwise.
	 * @throws AppleScriptException
	 * 		Thrown if there is an error marking or resetting the Reader while checking for more data.
	 */
	public boolean hasMore() throws AppleScriptException {
		if ( this.usePrevious ) {
			return true;
		}
		
		try {
			this.reader.mark(1);
		} catch (IOException e) {
			throw new AppleScriptException("Error marking the Reader position.", e);
		}
		
		try {
			if ( this.reader.read() <= 0 ) {
				return false;
			}
		} catch (IOException e) {
			throw new AppleScriptException("Error reading the next character from the Reader.", e);
		}
		
		try {
			this.reader.reset();
		} catch (IOException e) {
			throw new AppleScriptException("Error resetting the Reader position.", e);
		}
		
		return true;
	}
}