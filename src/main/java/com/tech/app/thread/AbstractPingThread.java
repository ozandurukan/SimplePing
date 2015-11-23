package com.tech.app.thread;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.tech.app.AppConstants;
import com.tech.app.exception.HTTPRequestException;
import com.tech.app.exception.UnixCommandException;
import com.tech.app.model.Host;
import com.tech.app.model.PingResult;
import com.tech.app.model.ReportObject;
import com.tech.app.service.CacheService;
import com.tech.app.service.HTTPRequestService;
import com.tech.app.service.UnixCommandService;

public abstract class AbstractPingThread extends Thread {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass()
			.getName());
	protected String hostName;
	protected CacheService cacheService;
	protected HTTPRequestService httpRequestService;
	protected UnixCommandService unixCommandService;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public AbstractPingThread(String hostName) {
		super();
		this.hostName = hostName;
		this.cacheService = CacheService.getCacheService();
		this.httpRequestService = HTTPRequestService.getHTTPRequestService();
		this.unixCommandService = UnixCommandService.getUnixCommandService();
	}
	
	

	public AbstractPingThread(String hostName, CacheService cacheService,
			HTTPRequestService httpRequestService, UnixCommandService unixCommandService) {
		super();
		this.hostName = hostName;
		this.cacheService = cacheService;
		this.httpRequestService = httpRequestService;
		this.unixCommandService = unixCommandService;
	}

	

	protected void saveLastResult(String jobType, String result) {
		cacheService.saveLastResult(hostName, jobType, result);
	}
	
	protected String runCommand(String command) throws UnixCommandException  {
		return unixCommandService.runCommand(command);
	}

	private ReportObject prepareReportData() {
		ReportObject report = new ReportObject();
		report.setHostName(hostName);
		Host tmpHost = cacheService.getHost(hostName);
		Iterator<Entry<String, PingResult>> it = tmpHost.getPingResultMap()
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, PingResult> pair = (Map.Entry<String, PingResult>) it
					.next();
			if (AppConstants.ICMP_JOB_TYPE.equals(pair.getKey())) {
				report.setIcmpPingResult(pair.getValue().getResult());
			} else if (AppConstants.TCP_JOB_TYPE.equals(pair.getKey())) {
				report.setTcpPingResult(pair.getValue().getResult());
			} else if (AppConstants.TRCRT_JOB_TYPE.equals(pair.getKey())) {
				report.setTracertResult(pair.getValue().getResult());
			} else {
				LOGGER.error("unknown ping type found in memory!");
			}
		}
		return report;
	}

	protected void sendReport() throws HTTPRequestException  {
		ReportObject rep = prepareReportData();
		Gson gson = new Gson();
		String jsonReport = gson.toJson(rep);
		LOGGER.warn("Sending report: {}",jsonReport);
		boolean isSuccess = httpRequestService.sendHttpPostRequest(AppConstants.REPORT_URL, jsonReport);
		if (isSuccess) {
			LOGGER.warn("Report sent successfully for host: {}", hostName);
		} else {
			LOGGER.warn("Report failed for host: {}", hostName);
		}
	}

	
}
