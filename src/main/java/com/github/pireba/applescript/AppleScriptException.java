package com.github.pireba.applescript;


/**
 * An {@code AppleScriptException} is thrown by the AppleScript classes when something goes wrong.
 */
public class AppleScriptException extends Exception {
	
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construct an {@code AppleScriptException} with the given message.
	 * 
	 * @param message
	 * 		The message.
	 */
	public AppleScriptException(String message) {
		super(message);
	}
	
	/**
	 * Construct an {@code AppleScriptException} with the given cause.
	 * 
	 * @param cause
	 * 		The cause.
	 */
	public AppleScriptException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Construct an {@code AppleScriptException} with the given message and cause.
	 * 
	 * @param message
	 * 		The message.
	 * @param cause
	 * 		The cause.
	 */
	public AppleScriptException(String message, Throwable cause) {
		super(message, cause);
	}
}