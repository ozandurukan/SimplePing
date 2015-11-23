package com.tech.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Test;

import com.tech.app.AppConstants;
import com.tech.app.service.CacheService;


public class CacheServiceTest {

	CacheService service = CacheService.getCacheService();

	@Test
	public void testSaveLastResultWhenCacheIsEmpty() {
		service.saveLastResult("google.com", AppConstants.ICMP_JOB_TYPE,
				"test result");
		assertNotNull(service.getHost("google.com"));
	}
	@Test
	public void testSaveLastResultWhenSameHostWithSamePingTypeExistsInCache() {
		String hostName = "google.com";
		service.saveLastResult(hostName, AppConstants.ICMP_JOB_TYPE,
				"test result");
		service.saveLastResult(hostName, AppConstants.ICMP_JOB_TYPE,
				"test result2");
		assertEquals("test result2",
				service.getHost(hostName).getPingResultMap()
						.get(AppConstants.ICMP_JOB_TYPE).getResult());
	}
	@Test
	public void testSaveLastResultWhenSameHostWithDifferentPingTypeExists() {
		String hostName = "google.com";
		service.saveLastResult(hostName, AppConstants.ICMP_JOB_TYPE,
				"test result");
		service.saveLastResult(hostName, AppConstants.TCP_JOB_TYPE,
				"test result2");
		assertEquals(2, service.getHost(hostName).getPingResultMap().size());
	}
	
	@Test(expected=CloneNotSupportedException.class)
	public void testClone() throws CloneNotSupportedException {
		service.clone();
	}

	@After
	public void clearCache() {
		service.clearCache();
	}
}
