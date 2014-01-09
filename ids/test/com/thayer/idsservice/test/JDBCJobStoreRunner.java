package com.thayer.idsservice.test;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class JDBCJobStoreRunner {

	public static void main(String args[]) {

		try {

			SchedulerFactory schedulerFactory = new StdSchedulerFactory();

			Scheduler scheduler = schedulerFactory.getScheduler();

			// ①获取调度器中所有的触发器组

			String[] triggerGroups = scheduler.getTriggerGroupNames();

			// ②重新恢复在tgroup1组中，名为trigger1_1触发器的运行

			for (int i = 0; i < triggerGroups.length; i++) {

				String[] triggers = scheduler.getTriggerNames(triggerGroups[i]);

				for (int j = 0; j < triggers.length; j++) {

					Trigger tg = scheduler.getTrigger(triggers[j], triggerGroups[i]);

					System.out.println(tg.getName());

				}

			}

			scheduler.start();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
