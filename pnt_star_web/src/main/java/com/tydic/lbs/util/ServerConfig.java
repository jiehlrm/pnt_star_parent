package com.tydic.lbs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.beanutils.PropertyUtils;
public class ServerConfig {
	public static Properties properties = null;
	private static String configFile = "spring/application.properties";
	public static Logger logger = Logger.getLogger(ServerConfig.class.getName());
	public ServerConfig() throws IOException {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() throws IOException,Exception {
		if (properties == null) {
			properties = new Properties();
			try {
				InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(configFile);
				properties.load(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	public String getValue(String proName)   {
		if (properties.getProperty(proName) == null)
			return "";
		return properties.getProperty(proName).toString();
	}
}
