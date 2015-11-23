package com.tech.app.thread;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.tech.app.AppConstants;
import com.tech.app.exception.HTTPRequestException;
import com.tech.app.exception.UnixCommandException;
import com.tech.app.model.Host;
import com.tech.app.model.PingResult;
import com.tech.app.service.CacheService;
import com.tech.app.service.HTTPRequestService;
import com.tech.app.service.UnixCommandService;

public class ICMPPingThreadTest {
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
				.thenReturn(" Lost = 0 ");

		ICMPPingThread icmp = new ICMPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		icmp.run();
		Mockito.verify(cacheService).saveLastResult(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());

	}

	@Test
	public void testICMPThreadRunWhenCommandFailsAndSendReportSucceeds()
			throws Exception {
		Mockito.when(unixCommandService.runCommand(Mockito.anyString()))
				.thenReturn(" Lost = 5 ");
		Mockito.when(
				httpRequestService.sendHttpPostRequest(Mockito.anyString(),
						Mockito.anyString())).thenReturn(true);

		ICMPPingThread icmp = new ICMPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		icmp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}

	@Test
	public void testICMPThreadRunWhenCommandFailsAndSendReportFails()
			throws Exception {

		Mockito.when(unixCommandService.runCommand(Mockito.anyString()))
				.thenReturn(" Lost = 5 ");
		Mockito.when(
				httpRequestService.sendHttpPostRequest(Mockito.anyString(),
						Mockito.anyString())).thenReturn(false);

		ICMPPingThread icmp = new ICMPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		icmp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}

	@Test
	public void testICMPThreadRunWhenCommandReturnsNull() throws Exception {

		Mockito.when(unixCommandService.runCommand(Mockito.anyString()))
				.thenReturn(null);
		Mockito.when(
				httpRequestService.sendHttpPostRequest(Mockito.anyString(),
						Mockito.anyString())).thenReturn(false);

		ICMPPingThread icmp = new ICMPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		icmp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}

	@Test
	public void testICMPThreadRunWhenCommandFailsWithPacketLoss()
			throws Exception {

		Mockito.when(unixCommandService.runCommand(Mockito.anyString()))
				.thenReturn("XXXX Lost = 5 XXXX");
		Mockito.when(
				httpRequestService.sendHttpPostRequest(Mockito.anyString(),
						Mockito.anyString())).thenReturn(false);

		ICMPPingThread icmp = new ICMPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		icmp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}

	@Test
	public void testICMPThreadRunWhenSendReportFailsWithException()
			throws Exception {

		Mockito.when(unixCommandService.runCommand(Mockito.anyString()))
				.thenReturn("XXXX Lost = 5 XXXX");
		Mockito.doThrow(new HTTPRequestException()).when(httpRequestService)
				.sendHttpPostRequest(Mockito.anyString(), Mockito.anyString());

		ICMPPingThread icmp = new ICMPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		icmp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}

	@Test
	public void testICMPThreadRunWhenCommandFailsWithError() throws Exception {

		Mockito.when(unixCommandService.runCommand(Mockito.anyString()))
				.thenReturn("host not found!");
		Mockito.when(
				httpRequestService.sendHttpPostRequest(Mockito.anyString(),
						Mockito.anyString())).thenReturn(true);

		ICMPPingThread icmp = new ICMPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		icmp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}

	@Test
	public void testICMPThreadRunWhenCommandFailsWithException() throws Exception {

		Mockito.doThrow(new UnixCommandException()).when(unixCommandService).runCommand(Mockito.anyString());

		ICMPPingThread icmp = new ICMPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		icmp.run();
		Mockito.verify(cacheService, Mockito.times(0)).saveLastResult(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

	}

}
