package com.github.vspro.cg.config;

import java.util.Properties;

public abstract class PropertyHolder {

	private Properties properties;

	public PropertyHolder() {
		super();
		this.properties = new Properties();
	}

	public void setProperty(String name, String value){
		this.properties.setProperty(name, value);
	}

	public String getProperty(String name){
		return this.properties.getProperty(name);
	}

}
