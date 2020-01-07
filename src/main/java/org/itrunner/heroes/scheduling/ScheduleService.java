package org.itrunner.heroes.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.calendar.WeeklyCalendar;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.itrunner.heroes.scheduling.Constants.*;

@Service
@Slf4j
public class ScheduleService {
    private final Scheduler scheduler;

    @Autowired
    public ScheduleService(Scheduler scheduler) {
        this.scheduler = scheduler;

        try {
            addJobListener();
            addCalendar();
            scheduleJob();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void unscheduleJob(String jobName) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, GROUP_NAME));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, GROUP_NAME));
    }

    public void triggerJob(String jobName) throws SchedulerException {
        scheduler.triggerJob(JobKey.jobKey(jobName, GROUP_NAME));
    }

    private void addJobListener() throws SchedulerException {
        UnscheduleJobListener jobListener = new UnscheduleJobListener();
        GroupMatcher<JobKey> groupMatcher = GroupMatcher.jobGroupEquals(GROUP_NAME);
        this.scheduler.getListenerManager().addJobListener(jobListener, groupMatcher);
    }

    private void addCalendar() throws SchedulerException {
        WeeklyCalendar calendar = new WeeklyCalendar();
        calendar.setDayExcluded(1, true);
        calendar.setDayExcluded(7, false);
        this.scheduler.addCalendar("weekly", calendar, false, false);
    }

    private void scheduleJob() throws SchedulerException {
        JobDetail jobDetail = createJobDetail();
        Trigger trigger = createTrigger(jobDetail);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private JobDetail createJobDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JOB_NAME, "getHeroes");
        jobDataMap.put(JOB_REST_URI, "http://localhost:8080/api/heroes/?name=Magma");
        jobDataMap.put(JOB_REQUEST_METHOD, "GET");

        return JobBuilder.newJob(RestJob.class).withIdentity("getHeroes", GROUP_NAME).usingJobData(jobDataMap).storeDurably().build();
    }

    private Trigger createTrigger(JobDetail jobDetail) {
        DailyTimeIntervalScheduleBuilder scheduleBuilder = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withIntervalInMinutes(1).onEveryDay();
        return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity("getHeroes", GROUP_NAME).withSchedule(scheduleBuilder).modifiedByCalendar("weekly").build();
    }

}
