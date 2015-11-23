package com.tech.app.thread;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.tech.app.AppConstants;
import com.tech.app.exception.UnixCommandException;
import com.tech.app.model.Host;
import com.tech.app.model.PingResult;
import com.tech.app.service.CacheService;
import com.tech.app.service.HTTPRequestService;
import com.tech.app.service.UnixCommandService;

public class TracertThreadTest {
	HTTPRequestService httpRequestService;
	CacheService cacheService;
	UnixCommandService unixCommandService;

	@Before
	public void setUp() throws Exception {
		httpRequestService = Mockito.mock(HTTPRequestService.class);
		cacheService = Mockito.mock(CacheService.class);
		unixCommandService = Mockito.mock(UnixCommandService.class);

		ConcurrentHashMap<String, PingResult> resultMap = new ConcurrentHashMap<String, PingResult>();
		PingResult resultForIcmp = new PingResult(AppConstants.ICMP_JOB_TYPE,
				"testResultForICMP", new Date());
		resultMap.put(AppConstants.ICMP_JOB_TYPE, resultForIcmp);
		PingResult resultForTcp = new PingResult(AppConstants.TCP_JOB_TYPE,
				"testResultForTCP", new Date());
		resultMap.put(AppConstants.TCP_JOB_TYPE, resultForTcp);
		PingResult resultForTracert = new PingResult(
				AppConstants.TRCRT_JOB_TYPE, "testResultForTracert", new Date());
		resultMap.put(AppConstants.TRCRT_JOB_TYPE, resultForTracert);
		Host host = new Host("google.com");
		host.setPingResultMap(resultMap);
		Mockito.when(cacheService.getHost("google.com")).thenReturn(host);
		

	}

	@Test
	public void testICMPThreadRunWhenCommandSucceeds() throws Exception {
		Mockito.when(unixCommandService.runCommand(Mockito.anyString()))
		.thenReturn(" Tracing route to google.com [216.58.211.14] ");
		TracertThread tracert = new TracertThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		tracert.run();
		Mockito.verify(cacheService).saveLastResult(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());

	}
	
	@Test
	public void testICMPThreadRunWhenCommandFailsWithException() throws Exception {
		Mockito.doThrow(new UnixCommandException()).when(unixCommandService).runCommand(Mockito.anyString());
		TracertThread tracert = new TracertThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		tracert.run();

		Mockito.verify(cacheService, Mockito.times(0)).saveLastResult(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

	}

	
}
