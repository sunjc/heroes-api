package org.itrunner.heroes.scheduling;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    private static final String CRON_EXPRESSION = "0 0/5 * * * ?";
    private static final String GROUP = "iTRunner";

    @Bean
    public Trigger helloJobTrigger(JobDetail helloJob) {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(CRON_EXPRESSION);
        return TriggerBuilder.newTrigger().forJob(helloJob).withIdentity(getTriggerKey(helloJob.getKey())).withSchedule(scheduleBuilder).build();
    }

    @Bean
    public JobDetail helloJob() {
        return JobBuilder.newJob(HelloQuartz.class).withIdentity(getJobKey(HelloQuartz.class)).storeDurably().build();
    }

    private static <T> JobKey getJobKey(Class<T> cls) {
        return new JobKey(cls.getSimpleName(), GROUP);
    }

    private static TriggerKey getTriggerKey(JobKey jobKey) {
        return new TriggerKey(jobKey.getName(), GROUP);
    }
}
