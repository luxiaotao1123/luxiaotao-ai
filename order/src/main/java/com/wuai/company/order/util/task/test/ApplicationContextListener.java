package com.wuai.company.order.util.task.test;

        import com.wuai.company.order.util.task.SimpleJob;
        import  org.quartz.*;
        import  org.quartz.impl.StdSchedulerFactory;
        import  org.slf4j.Logger;
        import  org.slf4j.LoggerFactory;

        import  javax.servlet.ServletContextEvent;
        import  javax.servlet.ServletContextListener;

/**
   *  自定义一个应用监听器
   */
public  class  ApplicationContextListener  implements  ServletContextListener  {
                private  Logger  logger  =  LoggerFactory.getLogger(this.getClass());

                public  static  Scheduler  scheduler  =  null;

                @Override
        public  void  contextInitialized(ServletContextEvent  servletContextEvent)  {
                        this.logger.info("Web应用开始...");

                        /*  注册定时任务  */
                        try  {
                                //  获取Scheduler实例
                                scheduler  =  StdSchedulerFactory.getDefaultScheduler();
                                scheduler.start();

                                //  具体任务
                                JobDetail  job  =  JobBuilder.newJob(SimpleJob.class).withIdentity("job1",  "group1").build();

                                //  触发时间点
                                SimpleScheduleBuilder  simpleScheduleBuilder  =  SimpleScheduleBuilder.simpleSchedule()
                                                .withIntervalInSeconds(5).repeatForever();
                                                
                                Trigger  trigger  =  TriggerBuilder.newTrigger().withIdentity("trigger1",  "group1")
                                                .startNow().withSchedule(simpleScheduleBuilder).build();

                                //  交由Scheduler安排触发
                                scheduler.scheduleJob(job,  trigger);

                                this.logger.info("调度器开始注册：The  scheduler  register...");
                        }  catch  (SchedulerException  se)  {
                                logger.error(se.getMessage(),  se);
                        }
                }

                @Override
        public  void  contextDestroyed(ServletContextEvent  servletContextEvent)  {
                        this.logger.info("Web应用停止...");

                        /*  注销定时任务  */
                        try  {
                                //  关闭Scheduler
                                scheduler.shutdown();

                                this.logger.info("调度器已关闭：The  scheduler  shutdown...");
                        }  catch  (SchedulerException  se)  {
                                logger.error(se.getMessage(),  se);
                        }
                }
        }


