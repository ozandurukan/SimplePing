package com.tech.app.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tech.app.AppConstants;
import com.tech.app.AppUtils;
import com.tech.app.exception.HTTPRequestException;
import com.tech.app.exception.UnixCommandException;
import com.tech.app.service.CacheService;
import com.tech.app.service.HTTPRequestService;
import com.tech.app.service.UnixCommandService;

public class ICMPPingThread extends AbstractPingThread {
	static final Logger LOGGER = LoggerFactory.getLogger(ICMPPingThread.class);

	@Override
	public void run() {
		LOGGER.debug("invoking ICMP Ping for host name: {}", hostName);
		String output = "";
		String command = "";
		try {
			command = AppConstants.ICMP_PING_COMMAND + " " + hostName;
			output = super.runCommand(command);
			LOGGER.debug(output);
			saveLastResult(AppConstants.ICMP_JOB_TYPE, output);
			processCommandResponse(output);
		} catch (UnixCommandException e) {
			LOGGER.error("Exception occured while running the command: {}",
					command, e);
		}


	}

	private void processCommandResponse(String response) {
		boolean shouldCallReport = false;

		if (!AppUtils.isStringNull(response)) {
			Integer index = response.indexOf("Lost = ");
			if (index > -1) {
				try {
					// it means command did not return any errors
					// for windows: Lost = 0 (0% loss)
					String packetLossFirstDigit = response.substring(index + 7,
							index + 8);
					if (!AppUtils.isStringNull(packetLossFirstDigit)
							&& Integer.parseInt(packetLossFirstDigit) > 0) {
						shouldCallReport = true;
					}
				} catch (Exception e) {
					LOGGER.error("Error while handling the response: {}",
							response, e);
				}
			} else {
				shouldCallReport = true;
			}
		} else {
			shouldCallReport = true;
		}
		if (shouldCallReport) {
			LOGGER.debug("Problematic case for response: {} Calling Report",
					response);
			try {
				sendReport();
			} catch (HTTPRequestException e) {
				LOGGER.error("Error while sending report for host: {}",
						hostName, e);
			}
		}
	}

	public ICMPPingThread(String hostName) {
		super(hostName);
	}

	public ICMPPingThread(String hostName, CacheService cacheService,
			HTTPRequestService httpRequestService,
			UnixCommandService unixCommandService) {
		super(hostName, cacheService, httpRequestService, unixCommandService);
	}

}
