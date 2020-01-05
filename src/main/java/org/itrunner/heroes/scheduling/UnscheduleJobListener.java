package org.itrunner.heroes.scheduling;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnscheduleJobListener implements JobListener {
    private static Logger log = LoggerFactory.getLogger(UnscheduleJobListener.class);

    @Override
    public String getName() {
        return "HERO_UnscheduleJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info(getJobName(context) + " is about to be executed.");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.info(getJobName(context) + " Execution was vetoed.");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.info(getJobName(context) + " was executed.");

        Boolean stop = (Boolean) context.get(Constants.JOB_STOP_FLAG);

        if (stop == null || !stop) {
            return;
        }

        String jobName = getJobName(context);

        log.info("Unschedule " + jobName);

        try {
            context.getScheduler().unscheduleJob(context.getTrigger().getKey());
        } catch (SchedulerException e) {
            log.error("Unable to unschedule " + jobName, e);
        }
    }

    private String getJobName(JobExecutionContext context) {
        return "Hero job " + context.getJobDetail().getKey().getName();
    }
}
