package com.github.vspro.cg.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public  class Messages {

	private Messages(){}

	private static final String BUNDLE_NAME = "com/github/vspro/cg/util/messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);


	public static String getString(String key){
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getString(String key, Object ...args){
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), args);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static void main(String[] args) {
		System.out.println(Messages.getString("ValidationError.4"));
		System.out.println(Messages.getString("ValidationError.0", "hello"));
	}




}
