package com.tech.app.service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.tech.app.model.Host;
import com.tech.app.model.PingResult;

public class CacheService {
	private static CacheService cacheService;
	private ConcurrentHashMap<String, Host> map;

	private CacheService() {
		map = new ConcurrentHashMap<String, Host>();
	}

	public static synchronized CacheService getCacheService() {
		if (cacheService == null) {
			cacheService = new CacheService();
		}
		return cacheService;
	}

	// to make sure object is singleton
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public void saveLastResult(String hostName, String pingType, String result) {
		PingResult tmpPingResult = new PingResult(pingType, result, new Date());
		Host tmpHost = map.get(hostName);
		if (tmpHost != null) {
			ConcurrentMap<String, PingResult> pingResultMap = tmpHost.getPingResultMap();
			PingResult resultObj = pingResultMap.get(pingType);
			if (resultObj != null) {
				pingResultMap.remove(pingType);
			} 
			pingResultMap.put(pingType, tmpPingResult);
		} else {
			//no hosts before
			tmpHost = new Host(hostName);
			tmpHost.getPingResultMap().put(pingType, tmpPingResult);
			map.put(hostName, tmpHost);
		}
	}

	public Host getHost(String hostName) {
		return map.get(hostName);
	}
	
	public void clearCache() {
		map.clear();
	}
}
