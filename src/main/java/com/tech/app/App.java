package com.tech.app;

import java.util.Timer;
import java.util.TimerTask;

import com.tech.app.task.MasterPingTask;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Pinging started. Please follow log files for details." );
        for (String jobType : AppConstants.JOB_TYPE_LIST) {
			Timer timer = new Timer();
			TimerTask task = new MasterPingTask(jobType);
			timer.schedule(task, AppConstants.INITIAL_DELAY, AppConstants.INTERVAL);
		}
    }
}
