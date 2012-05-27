package utils;

import org.apache.commons.lang.StringUtils;

import play.templates.JavaExtensions;

public class CustomExtensions extends JavaExtensions {

	public static String abbreviate(String text, int size) {
		return StringUtils.abbreviate(text, size);
	}

}
