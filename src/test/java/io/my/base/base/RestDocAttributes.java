package io.my.base.base;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public class RestDocAttributes {

	private RestDocAttributes() {
	    throw new IllegalStateException("Utility class");
	}
	
	public static Attributes.Attribute length(String length) {
		return key("length").value(length);
	}
	public static Attributes.Attribute length(int length) {
		return key("length").value(String.valueOf(length));
	}
	
	public static Attributes.Attribute length(int min, int max) {
		return key("length").value(min + "~" + max);
	}
	
	public static Attributes.Attribute format(String format) {
		return key("format").value(format);
	}
}
