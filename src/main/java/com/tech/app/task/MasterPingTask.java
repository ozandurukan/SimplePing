package com.tech.app.task;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tech.app.AppConstants;
import com.tech.app.exception.GeneralPingException;
import com.tech.app.thread.ICMPPingThread;
import com.tech.app.thread.TCPPingThread;
import com.tech.app.thread.TracertThread;

public class MasterPingTask extends TimerTask {

	static final Logger LOGGER = LoggerFactory.getLogger(MasterPingTask.class);
	private String jobType;

	@Override
	public void run() {

		try {
			Runnable thread;
			ExecutorService pool = Executors
			.newFixedThreadPool(AppConstants.HOST_NAMES.size());
			for (String hostName : AppConstants.HOST_NAMES) {
				if (AppConstants.ICMP_JOB_TYPE.equals(jobType)) {
					thread = new ICMPPingThread(hostName);
				} else if (AppConstants.TCP_JOB_TYPE.equals(jobType)) {
					thread = new TCPPingThread(hostName);
				} else if (AppConstants.TRCRT_JOB_TYPE.equals(jobType)) {
					thread = new TracertThread(hostName);
				} else {
					throw new GeneralPingException(
							"Unimplemented jobtype found in app config: "
									+ jobType);
				}
				pool.execute(thread);
			}

			pool.shutdown();
			// we wait until all the threads are done
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			LOGGER.error("Thread exception!", e);
		} catch (Exception e) {
			LOGGER.error("General exception!", e);
		}

	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public MasterPingTask(String jobType) {
		super();
		this.jobType = jobType;
	}

}
