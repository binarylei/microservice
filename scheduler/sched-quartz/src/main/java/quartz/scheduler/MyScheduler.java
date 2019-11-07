package quartz.scheduler;

import quartz.job.MyJob1;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import quartz.job.MyJob2;

public class MyScheduler {
	public static void main(String[] args) throws SchedulerException {

		// JobDetail
		JobDetail jobDetail = JobBuilder.newJob(MyJob1.class)
				.withIdentity("job1", "group1")
				.usingJobData("gupao","只为更好的你")
				.usingJobData("moon",5.21F)
				.build();

		// Trigger
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger1", "group2")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(2)
						.repeatForever())
				.build();

		JobDetail jobDetail2 = JobBuilder.newJob(MyJob2.class)
				.withIdentity("job2", "group1")
				.usingJobData("gupao","只为更好的你")
				.usingJobData("moon",5.21F)
				.build();

		// Trigger
		Trigger trigger2 = TriggerBuilder.newTrigger()
				.withIdentity("trigger2", "group2")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(10)
						.repeatForever())
				.build();

		// SchedulerFactory
		SchedulerFactory  factory = new StdSchedulerFactory();
		Scheduler scheduler = factory.getScheduler();

		// 绑定关系是1：N
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.scheduleJob(jobDetail2, trigger2);
		scheduler.start();
	}

}
