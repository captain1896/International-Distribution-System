package com.thayer.idsservice.test;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.thayer.idsservice.task.interf.IResvTask;

public class TestJob implements IResvTask, StatefulJob {
	private static int i = 0;
	private Scheduler scheduler;

	public TestJob() {
		// TODO Auto-generated constructor stub
	}

	public void doTask() {
		System.out.println("start doTask");
		System.out.println("=================================>" + i++);
		if (i % 5 == 0) {
			System.out.println("++++++++++++++ Start test ");
			try {
				System.out.println("++++++++++++++ Start pause ");
				scheduler.pauseTrigger("testTrigger", Scheduler.DEFAULT_GROUP);
				System.out.println("------------ Start sleep 10s ");
				Thread.sleep(10000);
				System.out.println("------------ end sleep 10s ");
				scheduler.resumeTrigger("testTrigger", Scheduler.DEFAULT_GROUP);
				System.out.println("++++++++++++++ end pause ");
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("=================================>" + i++);
		if (i % 5 == 0) {
			System.out.println("++++++++++++++ Start test ");
			try {
				Scheduler scheduler = arg0.getScheduler();
				System.out.println("++++++++++++++ Start pause ");
				scheduler.pauseTrigger("testTrigger", Scheduler.DEFAULT_GROUP);
				System.out.println("------------ Start sleep 10s ");
				Thread.sleep(10000);
				System.out.println("------------ end sleep 10s ");
				scheduler.resumeTrigger("testTrigger", Scheduler.DEFAULT_GROUP);
				System.out.println("++++++++++++++ end pause ");
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public SchedulerFactoryBean getSchedulerFactoryBean() {
		// TODO Auto-generated method stub
		return null;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
}
