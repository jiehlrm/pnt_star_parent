package com.tydic.pntstar.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;

public class ServerConfig {
	public static Properties properties = null;
	private static String configFile = "spring/application.properties";
	public static Properties kog4jproperties = null;
	public static Logger logger = Logger.getLogger(ServerConfig.class.getName());
	public ServerConfig() throws IOException {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() throws IOException,Exception {
		if (properties == null) {
			properties = new Properties();
			try {
				InputStream in = ServerConfig.class.getClassLoader().getResourceAsStream(configFile);
				properties.load(in);
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void initLog4j(String log4jconfigFile) throws IOException {
		if (kog4jproperties == null) {
			kog4jproperties = new Properties();
			InputStream log4jin = ServerConfig.class.getClassLoader()
					.getResourceAsStream(log4jconfigFile);
			kog4jproperties.load(log4jin);
			PropertyConfigurator.configure(kog4jproperties);
		}
	}

	public String getValue(String proName)   {
		if (properties.getProperty(proName) == null)
			return "";
		return properties.getProperty(proName).toString();
	}
}
