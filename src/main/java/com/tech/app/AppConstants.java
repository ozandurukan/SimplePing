package com.tech.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppConstants {
	public static final String ICMP_JOB_TYPE = "ICMP";
	public static final String TCP_JOB_TYPE = "TCP";
	public static final String TRCRT_JOB_TYPE = "TRACERT";
	public static final List<String> HOST_NAMES;
	public static final List<String> JOB_TYPE_LIST;
	public static final String ICMP_PING_COMMAND;
	public static final String REPORT_URL;
	public static final Integer HTTP_TIMEOUT;
	public static final Integer INITIAL_DELAY;
	public static final Integer INTERVAL;
	public static final Long TCP_TIME_THRESHOLD;
	public static final String TRACERT_COMMAND;
	public static final	Integer COMMAND_TIMEOUT_IN_SEC;
	static final Logger LOGGER = LoggerFactory.getLogger(AppConstants.class);
	
	static {
		Properties prop = readPropertyFile();
		HOST_NAMES = Arrays.asList(prop.getProperty("hostNames").split(";"));
		JOB_TYPE_LIST = Arrays.asList(prop.getProperty("jobTypeList").split(";"));
		ICMP_PING_COMMAND = prop.getProperty("icmpPingCommand");
		REPORT_URL = prop.getProperty("reportUrl");
		HTTP_TIMEOUT = Integer.parseInt(prop.getProperty("httpTimeout"));
		INITIAL_DELAY = Integer.parseInt(prop.getProperty("initialDelay"));
		INTERVAL = Integer.parseInt(prop.getProperty("interval"));
		TCP_TIME_THRESHOLD = Long.parseLong(prop.getProperty("tcpTimeThreshold"));
		TRACERT_COMMAND = prop.getProperty("tracertCommand");
		COMMAND_TIMEOUT_IN_SEC = Integer.parseInt(prop.getProperty("commandTimeoutInSec"));
	}

	private AppConstants() {
		
	}
	private static Properties readPropertyFile() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			String filename = "config.properties";
			input = App.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				throw new IOException("file not found!");
			}

			prop.load(input);
			LOGGER.debug("props loaded!");

		} catch (IOException ex) {
			LOGGER.error("Exception!",ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error("Exception!",e);
				}
			}
		}
		return prop;
	}
}
