package com.wuai.company.order.util.task;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.CronCalendar;
import org.quartz.impl.calendar.WeeklyCalendar;
import org.quartz.simpl.SimpleJobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.quartz.DateBuilder.dateOf;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by hyf on 2017/10/25.
 */
@Component
public class TimeTask {
    Logger logger = LoggerFactory.getLogger(TimeTask.class);
    @Scheduled(cron = "10 * * * * ?") //每小时执行一次
    public void execute() throws  Exception{
        Calendar calendar = Calendar.getInstance();
        Date currentStartTime = calendar.getTime();
        if(isWorkday(currentStartTime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
            String date = simpleDateFormat.format(new Date());
            logger.info("执行时间 date=",date);
        }
    }

    private boolean isWorkday(Date runDate) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        WeeklyCalendar weeklyCalendar = new WeeklyCalendar();
        weeklyCalendar.setDayExcluded(Calendar.THURSDAY,true);
        sched.addCalendar("weeklys", weeklyCalendar, false, false);
//        Date runDate = new Date();
        System.out.println("任务开始时间：" + sdf.format(runDate));

        JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1").build();
        SimpleTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runDate)
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(10).repeatForever())
                .modifiedByCalendar("cronCalendar")
                .build();

        Date firstRunTime = sched.scheduleJob(job, trigger);
        System.out.println(job.getKey() + " 将运行于：" + sdf.format(firstRunTime) + " 并重复：" + trigger.getRepeatCount() + " 次, 间隔 " + trigger.getRepeatInterval() / 1000 + " 秒");
        System.out.println("------- 开始 Scheduler ----------------");
        sched.start();

        try {
            System.out.println("------- 等待 120 秒（2分钟）... --------------");
            Thread.sleep(20L * 1000L);
            // do something
        } catch (Exception e) {
        }
        sched.shutdown(true);
        System.out.println("------- 关闭调度器 -----------------");

        SchedulerMetaData metaData = sched.getMetaData();
        System.out.println("~~~~~~~~~~  执行了 " + metaData.getNumberOfJobsExecuted() + " 个 jobs.");

        return true;
    }

    public static void main(String[] args) throws Exception {
        TimeTask timeTask= new TimeTask();
        timeTask.execute();
    }

}
