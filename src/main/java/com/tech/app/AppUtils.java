package com.tech.app;


public class AppUtils {
	
	private AppUtils() {
		
	}
	
	// I could have used apache commons but I wanted to avoid using libraries
	// for the purpose
	public static boolean isStringNull(String str) {
		if (str != null && str.length() > 0) {
			return false;
		} else {
			return true;
		}
	}

}
