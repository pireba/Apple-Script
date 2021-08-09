# Apple-Script

[![Maven Central](https://img.shields.io/maven-central/v/com.github.pireba/applescript?color=success&logo=apachemaven&style=for-the-badge)](https://mvnrepository.com/artifact/com.github.pireba/applescript)
[![Javadoc](https://javadoc.io/badge2/com.github.pireba/applescript/javadoc.svg?color=success&logo=readthedocs&style=for-the-badge)](https://javadoc.io/doc/com.github.pireba/applescript) 

## Overview
With Apple-Script you can execute AppleScript commands in Java.

The result can be parsed into an AppleScriptObject.
This represents an object in AppleScript.
The AppleScript classes are converted to Java classes as follows:

| AppleScript class | Java classs | Object getter | Comment |
| --- | --- | --- | --- |
| alias | String | getAlias() | The return value is a HFS path string. You have to do the conversion to a POSIX path yourself. |
| boolean | boolean | getBoolean() | - |
| date | Date | getDate(format) | You must specify the date format in the getter. See the description about AppleScript dates below. |
| file | String | getFile() | The return value is a HFS path string. You have to do the conversion to a POSIX path yourself. |
| integer | int | getInteger() | - |
| list | AppleScriptList | getList() | Stored internally as a _Java ArrayList_. You can use the normal List methods. |
| number | int or double | getInteger() or getDouble() | The abstract class _number_ can represent an _integer_ or a _real_. |
| POSIX file | String | getString() | Because there is no visual difference in the output of a POSIX file and a text, the POSIX file is identified and stored as a string. You have to do the conversion to a Java File yourself. |
| POSIX path | String | getString() | Because there is no visual difference in the output of a POSIX path and a text, the POSIX path is identified and stored as a string. You have to do the conversion to a Java Path yourself. |
| real | double | getDouble() | - |
| record | AppleScriptMap | getMap() | Stored internally as a _Java HashMap_. You can use the normal Map methods. |
| text | String | getString() | - |

## How to use

**Execute an AppleScript file**

	try {
		File file = new File("/Users/phillip/Example.applescript");
		AppleScript as = new AppleScript(file);
		as.execute();
	} catch (AppleScriptException e) {
		e.printStackTrace();
	}

**Execute an AppleScript command**

	try {
		String command = "tell application \"iTunes\" to pause";
		AppleScript as = new AppleScript(command);
		as.execute();
	} catch (AppleScriptException e) {
		e.printStackTrace();
	}

**Execute an AppleScript multi-line command**

	try {
		String command[] = new String[3];
		command[0] = "tell application \"iTunes\"";
		command[1] = "get name of item 1 of selection";
		command[2] = "end tell";
		AppleScript as = new AppleScript(command);
		as.execute();
	} catch (AppleScriptException e) {
		e.printStackTrace();
	}

**Execute an AppleScript command and get the result as a string**

	try {
		String command = "tell application \"iTunes\" to pause";
		AppleScript as = new AppleScript(command);
		String result = as.executeAsString();
		System.out.println(result);
	} catch (AppleScriptException e) {
		e.printStackTrace();
	}

**Execute an AppleScript command and get the result as an AppleScriptObject**

	try {
		String command = "tell application \"iTunes\" to get name of item 1 of selection";
		AppleScript as = new AppleScript(command);
		AppleScriptObject result = as.executeAsObject();
		System.out.println(result.getString());
	} catch (AppleScriptException e) {
		e.printStackTrace();
	}

## AppleScript dates
The date format of an AppleScript date depends on the user's region setting.
In Mac OS, each user can configure his own date format.
I have not found a way to read out this format from the system settings.
That's why the date format must be given with every call of `getDate()`.
With the enum _AppleScriptDateFormat_ I collected some default formats.
You can either use one of them or define your own as _SimpleDateFormat_.

**Example with an AppleScriptDateFormat**

	try {
		...
		object.getDate(AppleScriptDateFormat.GERMAN);
	} catch ( AppleScriptException e ) {
		e.printStackTrace();
	}

**Example with a custom format**

	try {
		SimpleDateFormat format = new SimpleDateFormat("EEEE, d. MMMM y 'um' hh:mm:ss", Locale.GERMAN);
		object.getDate(format);
	} catch ( AppleScriptException e ) {
		e.printStackTrace();
	}
