package com.utils;

/**
 * Contains several utilities for converting strings between various cases and
 * formatting styles, for ease of reading.
 *
 * @author Christopher
 *
 */
public final class StringUtils {

	/**
	 * Converts an input string to title case, setting the first character of
	 * each word to upper case, and setting the remaining characters to lower
	 * case.
	 *
	 * @param input string to be converted.
	 * @return converted string.
	 */
	public static String titleCase(String input) {
		boolean capitalise = true;
		String outputStr = "";
		for (int i = 0; i < input.length(); i++) {
			if (capitalise) {
				outputStr += Character.toUpperCase(input.charAt(i));
				capitalise = false;
			} else
				outputStr += Character.toLowerCase(input.charAt(i));
			if (input.charAt(i) == ' ')
				capitalise = true;
		}
		return outputStr;
	}

}
