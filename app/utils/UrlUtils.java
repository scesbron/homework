package utils;

public class UrlUtils {
	public static final String encodePathSegment(String segment) {
		return segment == null ? "" : segment.replaceAll(" ", "%20");
	}
}
