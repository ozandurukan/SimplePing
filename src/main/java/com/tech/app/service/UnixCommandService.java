package com.tech.app.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tech.app.AppConstants;
import com.tech.app.exception.UnixCommandException;



public class UnixCommandService {
	private static UnixCommandService unixCommandService;
	private static final Logger LOGGER = LoggerFactory.getLogger(UnixCommandService.class);

	private UnixCommandService() {
	}

	public static synchronized UnixCommandService getUnixCommandService() {
		if (unixCommandService == null) {
			unixCommandService = new UnixCommandService();
		}
		return unixCommandService;
	}

	// to make sure object is singleton
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public String runCommand(String command) throws UnixCommandException {
		StringBuilder output = new StringBuilder();

		Process p;
		BufferedReader reader = null;
		try {
		p = Runtime.getRuntime().exec(command);
		if (!p.waitFor(AppConstants.COMMAND_TIMEOUT_IN_SEC, TimeUnit.SECONDS)) {
			p.destroy();
		}
		reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}
		
		} catch(IOException e) {
			throw new UnixCommandException("Command failed! ", e);
		} catch (InterruptedException e){
			throw new UnixCommandException("Thread interrupted!", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOGGER.error("Failed while closing reader!",e);
				}
			}
		}

		return output.toString();
	}

}
