package org.itrunner.heroes.scheduling;

import org.itrunner.heroes.util.DateUtils;
import org.quartz.*;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class QuartzConfig {
    private static final String CRON_EXPRESSION = "0 0/5 * * * ?";
    private static final String GROUP = "iTRunner";

    @Bean
    public Trigger helloJobTrigger(JobDetail helloJob) {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(CRON_EXPRESSION);
        return TriggerBuilder.newTrigger().forJob(helloJob).withIdentity(getTriggerKey(helloJob.getKey())).withSchedule(scheduleBuilder).modifiedByCalendar("holidayCalendar").build();
    }

    @Bean
    public JobDetail helloJob() {
        return JobBuilder.newJob(HelloQuartz.class).withIdentity(getJobKey(HelloQuartz.class)).storeDurably().build();
    }

    @Bean
    public Calendar holidayCalendar() {
        HolidayCalendar calendar = new HolidayCalendar();
        LocalDate date = LocalDate.of(2020, 1, 1);
        calendar.addExcludedDate(DateUtils.toDate(date));
        return calendar;
    }

    private static <T> JobKey getJobKey(Class<T> cls) {
        return new JobKey(cls.getSimpleName(), GROUP);
    }

    private static TriggerKey getTriggerKey(JobKey jobKey) {
        return new TriggerKey(jobKey.getName(), GROUP);
    }
}
