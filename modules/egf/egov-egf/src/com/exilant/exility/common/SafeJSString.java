package com.exilant.exility.common;

public class SafeJSString {

	private SafeJSString() {
		super();
	}
	
	public static String escape(String str){
		return str.replaceAll("'", "\\'").replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r");
	}

}
