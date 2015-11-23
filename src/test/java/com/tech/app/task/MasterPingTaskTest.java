//package com.tech.app.task;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import com.tech.app.AppConstants;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({MasterPingTask.class})
//public class MasterPingTaskTest {
//
//
//  //I cant get it to work right now. It might be an issue with Powermockito and Executors class
//	@Test
//	public void testMasterPingTask(){
//		ExecutorService pool = Mockito.mock(ExecutorService.class);
//		PowerMockito.mockStatic(Executors.class);
//		Mockito.when(Executors.newFixedThreadPool(Mockito.anyInt()))
//				.thenReturn(pool);
//		for (String jobType : AppConstants.JOB_TYPE_LIST) {
//			MasterPingTask task = new MasterPingTask(jobType);
//			task.run();
//		}
//		Mockito.verify(
//				pool,
//				Mockito.times((AppConstants.JOB_TYPE_LIST.size())
//						* AppConstants.HOST_NAMES.size())).execute(Mockito.<MasterPingTask>any());
//
//	}
//
//}
