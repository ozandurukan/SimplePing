package com.tech.app.thread;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.tech.app.AppConstants;
import com.tech.app.exception.HTTPRequestException;
import com.tech.app.model.Host;
import com.tech.app.model.PingResult;
import com.tech.app.service.CacheService;
import com.tech.app.service.HTTPRequestService;
import com.tech.app.service.UnixCommandService;

public class TCPPingThreadTest {
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
	public void testTCPThreadRunWhenHTTPGetSucceeds() throws Exception {

		Mockito.when(httpRequestService.sendHttpGetRequest(Mockito.anyString()))
				.thenReturn(200);

		TCPPingThread tcp = new TCPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		tcp.run();
		Mockito.verify(httpRequestService, Mockito.times(0))
				.sendHttpPostRequest(Mockito.anyString(), Mockito.anyString());

	}
	
	@Test
	public void testTCPThreadRunWhenHTTPGetReturnsEmptyResponse()
			throws Exception {

		Mockito.doThrow(new HTTPRequestException()).when(httpRequestService)
		.sendHttpGetRequest(Mockito.anyString());

		TCPPingThread tcp = new TCPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		tcp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}


	
	@Test
	public void testTCPThreadRunWhenHTTPGetReturnsConnectionTimeoutException()
			throws Exception {

		Mockito.doThrow(new HTTPRequestException()).when(httpRequestService)
				.sendHttpGetRequest(Mockito.anyString());

		TCPPingThread tcp = new TCPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		tcp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}

	@Test
	public void testTCPThreadRunWhenSendReportFailsWithException()
			throws Exception {
		
		Mockito.when(httpRequestService.sendHttpGetRequest(Mockito.anyString()))
				.thenReturn(500);

		Mockito.doThrow(new HTTPRequestException()).when(httpRequestService)
				.sendHttpPostRequest(Mockito.anyString(), Mockito.anyString());

		TCPPingThread tcp = new TCPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		tcp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}
	
	@Test
	public void testTCPThreadRunWhenHTTPGetTakesTooLong()
			throws Exception {
		
		Mockito.when(httpRequestService.sendHttpGetRequest(Mockito.anyString()))
				.then(new Answer<Integer>() {
					   @Override
					   public Integer answer(InvocationOnMock invocation) throws InterruptedException{
					     Thread.sleep(500);
					     return 200;
					   };
				});

		Mockito.doThrow(new HTTPRequestException()).when(httpRequestService)
				.sendHttpPostRequest(Mockito.anyString(), Mockito.anyString());

		TCPPingThread tcp = new TCPPingThread("google.com", cacheService,
				httpRequestService, unixCommandService);
		tcp.run();
		Mockito.verify(httpRequestService).sendHttpPostRequest(
				Mockito.anyString(), Mockito.anyString());

	}

}
