package com.lab.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RELoadConfig {

	final Properties properties = new Properties();
	
	private static RELoadConfig conf;
	
	public RELoadConfig() throws IOException {
	
		try (final InputStream stream = this.getClass().getResourceAsStream("rebot.properties")) {
		    properties.load(stream);
		}
	}
	
	public static RELoadConfig getInstance() throws IOException {
		if (conf == null) conf = new RELoadConfig();
		return conf;
	}
	
	public String getParameterValue(String parName) {
		return properties.getProperty(parName);
	}

}
