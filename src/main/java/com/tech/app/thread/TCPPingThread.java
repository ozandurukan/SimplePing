package com.tech.app.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tech.app.AppConstants;
import com.tech.app.exception.HTTPRequestException;
import com.tech.app.service.CacheService;
import com.tech.app.service.HTTPRequestService;
import com.tech.app.service.UnixCommandService;

public class TCPPingThread extends AbstractPingThread {

	static final Logger LOGGER = LoggerFactory.getLogger(TCPPingThread.class);
	static final String prefix = "http://www.";

	@Override
	public void run() {
		LOGGER.debug("invoking TCP Ping for host name: {}", hostName);
		// We set the status -1 for timeouts
		Integer statusCode = -1;
		Long duration = 0L;
		try {
			Long startTime = System.currentTimeMillis();
			statusCode = httpRequestService.sendHttpGetRequest(prefix + hostName);
			duration = System.currentTimeMillis() - startTime;
		} catch (HTTPRequestException e) {
			LOGGER.error("Exception for host: {}", hostName, e);
		} 
		String result = new StringBuilder().append("hostName: ")
				.append(hostName).append(" statusCode: ").append(statusCode)
				.append(" responseTime: ").append(duration).toString();
		saveLastResult(AppConstants.TCP_JOB_TYPE, result);
		LOGGER.debug(result);
		if (statusCode != 200 || duration > AppConstants.TCP_TIME_THRESHOLD) {
			try {
				sendReport();
			} catch (HTTPRequestException e) {
				LOGGER.error("Error while sending report for host: {}",
						hostName, e);
			}
		}
	}

	public TCPPingThread(String hostName) {
		super(hostName);
	}

	public TCPPingThread(String hostName, CacheService cacheService,
			HTTPRequestService httpRequestService,
			UnixCommandService unixCommandService) {
		super(hostName, cacheService, httpRequestService, unixCommandService);
	}


}
