package com.tech.app.service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tech.app.AppConstants;
import com.tech.app.exception.HTTPRequestException;

public class HTTPRequestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPRequestService.class);
	private static final String USER_AGENT = "Mozilla/5.0";
	private HttpClient httpClient;
	private static HTTPRequestService httpRequestService;

	private HTTPRequestService() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(100);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(AppConstants.HTTP_TIMEOUT).build();
		httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(cm).build();
	}

	public static synchronized HTTPRequestService getHTTPRequestService() {
		if (httpRequestService == null) {
			httpRequestService = new HTTPRequestService();
		}
		return httpRequestService;
	}

	// to make sure object is singleton
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public boolean sendHttpPostRequest(String url, String postData) throws HTTPRequestException {

		HttpResponse response = null;
		boolean retVal = false;
		HttpPost request = new HttpPost(url);
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		try {
			StringEntity params = new StringEntity(postData);
			request.setEntity(params);
			response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				retVal = true;
			} else {
				retVal = false;
			}
			
		} catch (ConnectTimeoutException e) {
			throw new HTTPRequestException("Connection timed out!", e);
		} catch (IOException e) {
			throw new HTTPRequestException("Request failed!", e);
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					LOGGER.error("Failed to consume entity!", e);
				}
			}
		}
		return retVal;
	}

	public Integer sendHttpGetRequest(String url) throws HTTPRequestException  {

		HttpResponse response = null;
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", USER_AGENT);
		Integer statusCode = -1;
		try {
			response = httpClient.execute(request);
			if (response != null) {
				statusCode = response.getStatusLine().getStatusCode();
			}
		} catch (ConnectTimeoutException e) {
			throw new HTTPRequestException("Connection timed out!", e);
		} catch (IOException e) {
			throw new HTTPRequestException("Request failed!", e);
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					LOGGER.error("Failed to consume entity!", e);
				}
			}
		}
		return statusCode;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
