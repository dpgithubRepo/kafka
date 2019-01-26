package com.kafka.twitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {

	static Logger logger = LoggerFactory.getLogger(Configuration.class);
	private static final Properties properties;

	static {
		properties = new Properties();
		try (InputStream inputStream = Configuration.class.getResourceAsStream(IKafkaTwitterConstants.CONFIG_FILE)) {
			properties.load(inputStream);
		} catch (IOException e) {
			logger.error("Error while reading the properties file : {}", IKafkaTwitterConstants.CONFIG_FILE);
			throw new RuntimeException("Error while reading the properties file", e);
		}
	}

	public static String getConfigurationValue(String key) {
		return properties.getProperty(key);
	}

}
