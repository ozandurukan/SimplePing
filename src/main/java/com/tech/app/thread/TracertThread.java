package com.tech.app.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tech.app.AppConstants;
import com.tech.app.exception.UnixCommandException;
import com.tech.app.service.CacheService;
import com.tech.app.service.HTTPRequestService;
import com.tech.app.service.UnixCommandService;


public class TracertThread extends AbstractPingThread{

	static final Logger LOGGER = LoggerFactory.getLogger(TracertThread.class);
	@Override
	public void run() {
		LOGGER.debug("invoking Traceroute for host name: {}", hostName);
		String output = "";
		String command = "";
		try {
			command = AppConstants.TRACERT_COMMAND + " " + hostName;
			output = super.runCommand(command);
			LOGGER.debug(output);
			saveLastResult(AppConstants.TRCRT_JOB_TYPE, output);
		} catch (UnixCommandException e) {
			LOGGER.error("Exception occured while running the command: {}", command, e);
		}
	}

	public TracertThread(String hostName, CacheService cacheService,
			HTTPRequestService httpRequestService,
			UnixCommandService unixCommandService) {
		super(hostName, cacheService, httpRequestService, unixCommandService);
	}

	
	public TracertThread(String hostName) {
		super(hostName);
	}
	
	
	
}
