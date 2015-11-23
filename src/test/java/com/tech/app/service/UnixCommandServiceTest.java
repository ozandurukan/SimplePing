package com.tech.app.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.tech.app.service.UnixCommandService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UnixCommandService.class)
public class UnixCommandServiceTest {

	UnixCommandService service = UnixCommandService.getUnixCommandService();
	Process mockedProcess;

	@Before
	public void setUp() throws ClientProtocolException, IOException {

		Runtime mockedRuntime = Mockito.mock(Runtime.class);
		mockedProcess = Mockito.mock(Process.class);

		Mockito.when(mockedRuntime.exec(Mockito.anyString())).thenReturn(
				mockedProcess);

		PowerMockito.mockStatic(Runtime.class);
		Mockito.when(Runtime.getRuntime()).thenReturn(mockedRuntime);
		InputStream mockedInputStr = new ByteArrayInputStream(
				"XXX  Lost = 0  XXX".getBytes());
		InputStream mockedFailInputStr = new ByteArrayInputStream(
				"XXX  Lost = 5  XXX".getBytes());
		Mockito.when(mockedProcess.getInputStream()).thenReturn(mockedInputStr)
				.thenReturn(mockedFailInputStr);

	}

	@Test
	public void testRunCommandSuccessInShortTime() throws Exception {
		InputStream mockedInputStr = new ByteArrayInputStream(
				"XXX  Lost = 0  XXX".getBytes());
		Mockito.when(mockedProcess.getInputStream()).thenReturn(mockedInputStr)
				.thenReturn(mockedInputStr);
		Mockito.when(
				mockedProcess.waitFor(Mockito.anyLong(),
						Mockito.any(TimeUnit.class))).thenReturn(true);
		String result = service.runCommand("ping -n 5 google.com");
		assertTrue(result.contains("Lost = 0"));

	}

	@Test
	public void testRunCommandSuccessInLongTime() throws Exception {
		//since the process will be killed, the result is expected to be partial
		InputStream mockedInputStr = new ByteArrayInputStream(
				"XXX  Los...".getBytes());
		Mockito.when(mockedProcess.getInputStream()).thenReturn(mockedInputStr)
				.thenReturn(mockedInputStr);
		Mockito.when(
				mockedProcess.waitFor(Mockito.anyLong(),
						Mockito.any(TimeUnit.class))).thenReturn(false);

		String result = service.runCommand("ping -n 5 google.com");
		assertFalse(result.contains("Lost = 0"));

	}
	
	@Test
	public void testRunCommandFailInShortTime() throws Exception {
		InputStream mockedInputStr = new ByteArrayInputStream(
				"XXX  Lost = 5  XXX".getBytes());
		Mockito.when(mockedProcess.getInputStream()).thenReturn(mockedInputStr)
				.thenReturn(mockedInputStr);
		Mockito.when(
				mockedProcess.waitFor(Mockito.anyLong(),
						Mockito.any(TimeUnit.class))).thenReturn(true);

		String result = service.runCommand("ping -n 5 faildomain.com");
		assertTrue(result.contains("Lost = 5"));

	}
	
	@Test(expected=CloneNotSupportedException.class)
	public void testClone() throws CloneNotSupportedException {
		service.clone();
	}
	

}
