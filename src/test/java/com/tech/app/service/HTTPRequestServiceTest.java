package com.tech.app.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.tech.app.service.HTTPRequestService;

public class HTTPRequestServiceTest {

	HTTPRequestService httpRequestService = HTTPRequestService
			.getHTTPRequestService();

	@Before
	public void setUp() throws ClientProtocolException, IOException {
		HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
		HttpResponse response = Mockito.mock(HttpResponse.class);
		StatusLine mockLine = Mockito.mock(StatusLine.class);
		Mockito.when(mockLine.getStatusCode()).thenReturn(200).thenReturn(500);
		Mockito.when(response.getStatusLine()).thenReturn(mockLine);
		Mockito.when(mockHttpClient.execute(Mockito.<HttpPost> any()))
				.thenReturn(response);
		httpRequestService.setHttpClient(mockHttpClient);

	}

	@Test
	public void testSendHttpPostRequestSuccess() throws Exception {
		HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
		HttpResponse response = Mockito.mock(HttpResponse.class);
		StatusLine mockLine = Mockito.mock(StatusLine.class);
		Mockito.when(mockLine.getStatusCode()).thenReturn(200);
		Mockito.when(response.getStatusLine()).thenReturn(mockLine);
		Mockito.when(mockHttpClient.execute(Mockito.<HttpPost> any()))
				.thenReturn(response);
		httpRequestService.setHttpClient(mockHttpClient);
		assertTrue(httpRequestService.sendHttpPostRequest("google.com",
				"testData"));

	}

	@Test
	public void testSendHttpPostRequestFail() throws Exception {
		HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
		HttpResponse response = Mockito.mock(HttpResponse.class);
		StatusLine mockLine = Mockito.mock(StatusLine.class);
		Mockito.when(mockLine.getStatusCode()).thenReturn(500);
		Mockito.when(response.getStatusLine()).thenReturn(mockLine);
		Mockito.when(mockHttpClient.execute(Mockito.<HttpPost> any()))
				.thenReturn(response);
		httpRequestService.setHttpClient(mockHttpClient);
		assertFalse(httpRequestService.sendHttpPostRequest("google.com",
				"testData"));

	}

	@Test
	public void testSendHttpGetRequestWithSuccessResponse() throws Exception {
		HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
		HttpEntity mockHttpEntity = Mockito.mock(HttpEntity.class);
		HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
		StatusLine mockLine = Mockito.mock(StatusLine.class);
		Mockito.when(mockLine.getStatusCode()).thenReturn(200);
		Mockito.when(mockResponse.getStatusLine()).thenReturn(mockLine);
		Mockito.when(mockHttpClient.execute(Mockito.<HttpPost> any()))
				.thenReturn(mockResponse);
		Mockito.when(mockResponse.getEntity()).thenReturn(mockHttpEntity);
		httpRequestService.setHttpClient(mockHttpClient);
		httpRequestService.sendHttpGetRequest("google.com");
		Mockito.verify(mockHttpClient).execute(Mockito.any(HttpGet.class));

	}

	@Test
	public void testSendHttpGetRequestWithNullResponse() throws Exception {
		HttpClient mockHttpClient = Mockito.mock(HttpClient.class);

		Mockito.when(mockHttpClient.execute(Mockito.<HttpPost> any()))
				.thenReturn(null);
		
		httpRequestService.setHttpClient(mockHttpClient);
		httpRequestService.sendHttpGetRequest("google.com");
		Mockito.verify(mockHttpClient).execute(Mockito.any(HttpGet.class));

	}

	@Test(expected = CloneNotSupportedException.class)
	public void testClone() throws CloneNotSupportedException {
		httpRequestService.clone();
	}
}
