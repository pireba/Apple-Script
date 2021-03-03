package com.github.pireba.applescript;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This class can execute AppleScript commands and script files.
 * If the command produces output, it can be parsed into an {@linkplain AppleScriptObject} or retrieved as a string.
 * <br>
 * <pre>
 * Example call with an {@linkplain AppleScriptObject} as result:
 * {@code
 * AppleScript as = new AppleScript("tell application \"iTunes\" to get name of selection");
 * AppleScriptObject object = as.executeAsObject();
 * String result = object.getString();
 * }
 * 
 * Example call with a string as result:
 * {@code
 * AppleScript as = new AppleScript("tell application \"iTunes\" to get name of selection");
 * String result = as.executeAsString();
 * }
 * 
 * Example call without a result:
 * {@code
 * AppleScript as = new AppleScript("tell application \"iTunes\" to play");
 * as.execute();
 * }
 * </pre>
 */
public class AppleScript {
	
	/**
	 * Basic CLI command that is used to execute an AppleScript command.<br>
	 * <br>
	 * osascript =&gt; CLI command to execute AppleScript commands.<br>
	 * -ss       =&gt; Print values in recompilable source form.<br>
	 * -se       =&gt; Print script errors to stderr.
	 * 
	 * @see
	 * 		<a
	 * 		href="https://ss64.com/osx/osascript.html">
	 * 		osascript Man Page
	 * 		</a>
	 */ 
	private static final String[] BASIC_COMMAND = {"osascript", "-ss", "-se"};
	
	/**
	 * The full CLI command to execute.
	 */
	private final String[] command;
		
	/**
	 * Creates an AppleScript object for the given AppleScript command.
	 * 
	 * @param command
	 * 		The AppleScript command to execute.
	 * @throws IllegalArgumentException
	 * 		When the command is null or empty.
	 */
	public AppleScript(final String... command) {
		if ( command == null ) {
			throw new IllegalArgumentException("The command must not be null.");
		}
		
		if ( command.length == 0 ) {
			throw new IllegalArgumentException("The command must not be empty.");
		}
				
		this.command = this.prepareAsCommand(command);
	}
		
	/**
	 * Creates an AppleScript object for the given script file.
	 * 
	 * @param file
	 * 		The script file.
	 * @param arguments
	 * 		The arguments for the script.
	 * @throws IllegalArgumentException
	 * 		If the file or the arguments are null.
	 */
	public AppleScript(final File file, final String... arguments) {
		if ( file == null ) {
			throw new IllegalArgumentException("The file must not be null.");
		}
		
		if ( arguments == null ) {
			throw new IllegalArgumentException("The arguments must not be null.");
		}
		
		this.command = this.prepareAsScript(file, arguments);
	}
	
	/**
	 * Executes the AppleScript and returns the exit code of the execution.
	 * 
	 * @return
	 * 		The exit code of the execution.
	 * @throws AppleScriptException
	 * 		If an error occurs during execution.
	 */
	public int execute() throws AppleScriptException {
		ProcessBuilder builder = null;
		Process process = null;
		
		try {
			builder = new ProcessBuilder(this.command);
			process = builder.start();
			process.waitFor();
			
			if ( process.exitValue() != 0 ) {
				InputStream stream = process.getErrorStream();
				throw new AppleScriptException(this.streamToString(stream));
			}
			
			return process.exitValue();
		} catch ( Exception e ) {
			throw new AppleScriptException("Error executing AppleScript command.", e);
		} finally {
			try {
				process.getErrorStream().close();
				process.getInputStream().close();
				process.getOutputStream().close();
			} catch (Exception e) {
				// No exception handling.
			}
		}
	}
	
	/**
	 * Executes the AppleScript and parses the output into an {@linkplain AppleScriptObject}.
	 * 
	 * @return
	 * 		The {@linkplain AppleScriptObject}.
	 * @throws AppleScriptException
	 * 		If an error occurs during execution.
	 */
	public AppleScriptObject executeAsObject() throws AppleScriptException {
		ProcessBuilder builder = null;
		Process process = null;
		
		try {
			builder = new ProcessBuilder(this.command);
			process = builder.start();
			
			InputStream stream = process.getInputStream();
			AppleScriptTokenizer tokenizer = new AppleScriptTokenizer(stream);
			AppleScriptObject object = new AppleScriptObject(tokenizer);
			
			process.waitFor();
			
			if ( process.exitValue() != 0 ) {
				InputStream errorStream = process.getErrorStream();
				throw new AppleScriptException(this.streamToString(errorStream));
			}
			
			return object;
		} catch ( Exception e ) {
			throw new AppleScriptException("Error executing AppleScript command.", e);
		} finally {
			try {
				process.getErrorStream().close();
				process.getInputStream().close();
				process.getOutputStream().close();
			} catch (Exception e) {
				// No exception handling.
			}
		}
	}
	
	/**
	 * Executes the AppleScript and returns the output as a string.
	 * 
	 * @return
	 * 		The output.
	 * @throws AppleScriptException
	 * 		If an error occurs during execution.
	 */
	public String executeAsString() throws AppleScriptException {
		ProcessBuilder builder = null;
		Process process = null;
		
		try {
			builder = new ProcessBuilder(this.command);
			process = builder.start();
			process.waitFor();
			
			if ( process.exitValue() != 0 ) {
				InputStream errorStream = process.getErrorStream();
				throw new AppleScriptException(this.streamToString(errorStream));
			}
			
			InputStream stream = process.getInputStream();
			return this.streamToString(stream);
		} catch ( Exception e ) {
			throw new AppleScriptException("Error executing AppleScript command.", e);
		} finally {
			try {
				process.getErrorStream().close();
				process.getInputStream().close();
				process.getOutputStream().close();
			} catch (Exception e) {
				// No exception handling.
			}
		}
	}
	
	/**
	 * Gets the content of an {@linkplain InputStream} as a string.
	 * UTF-8 is used as encoding.
	 * 
	 * @param stream
	 * 		The {@linkplain InputStream} to read from.
	 * @return
	 * 		The string.
	 */
	private String streamToString(final InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		
		try {
			String line = null;
			while ( (line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch ( IOException e ) {
			
		}
		
		return sb.toString();
	}
	
	/**
	 * Prepares the specified AppleScript command for use as an operating system command.
	 * Every AppleScript command must be prepared with this function before it can be executed.<br>
	 * The given AppleScript command is combined with the content of the {@code BASE_COMMAND} variable.
	 * Also, each command line is prefixed with "-e".
	 * 
	 * <pre>
	 * Example:
	 * 
	 * ARRAY[0] = "tell application \"iTunes\""
	 * ARRAY[1] = "get name of selection"
	 * ARRAY[2] = "end tell"
	 * 
	 * The above command becomes:
	 * 
	 * ARRAY[0] = "osascript"
	 * ARRAY[1] = "-ss"
	 * ARRAY[2] = "-se"
	 * ARRAY[3] = "-e"
	 * ARRAY[4] = "tell application \"iTunes\""
	 * ARRAY[5] = "-e"
	 * ARRAY[6] = "get name of selection"
	 * ARRAY[7] = "-e"
	 * ARRAY[8] = "end tell"
	 * </pre>
	 * 
	 * @param commands
	 * 		An AppleScript command.
	 * @return
	 * 		The prepared CLI command.
	 */
	private String[] prepareAsCommand(String... commands) {
		int length = BASIC_COMMAND.length;
		length += commands.length * 2;
		
		String[] result = Arrays.copyOf(BASIC_COMMAND, length);
		
		int i = BASIC_COMMAND.length;
		for ( String s : commands ) {
			result[i] = "-e";
			i++;
			result[i] = s;
			i++;
		}
		
		return result;
	}
	
	/**
	 * Prepares the specified script call for use as an operating system command.
	 * Every script call must be prepared with this function before it can be executed.<br>
	 * The specified script call is combined with the contents of the {@code BASE_COMMAND} variable.
	 * 
	 * <pre>
	 * Example:
	 * 
	 * ARRAY[0] = example.applescript
	 * ARRAY[1] = arg1
	 * ARRAY[2] = arg2
	 * ARRAY[3] = arg3
	 * 
	 * The above command becomes:
	 * 
	 * ARRAY[0] = osascript
	 * ARRAY[1] = -ss
	 * ARRAY[2] = -se
	 * ARRAY[3] = example.applescript
	 * ARRAY[4] = arg1
	 * ARRAY[5] = arg2
	 * ARRAY[6] = arg3
	 * </pre>
	 * 
	 * @param file
	 * 		The script file.
	 * @param arguments
	 * 		The arguments for the script.
	 * @return
	 * 		The prepared CLI command.
	 */
	private String[] prepareAsScript(File file, String... arguments) {
		int length = BASIC_COMMAND.length;
		length += 1;
		length += arguments.length;
		
		String[] result = Arrays.copyOf(BASIC_COMMAND, length);
		
		int i = BASIC_COMMAND.length;
		result[i] = file.getPath();
		i++;
		
		for ( String s : arguments ) {
			result[i] = s;
			i++;
		}
		
		return result;
	}
}