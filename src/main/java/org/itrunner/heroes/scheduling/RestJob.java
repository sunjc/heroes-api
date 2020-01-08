package org.itrunner.heroes.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.itrunner.heroes.service.mail.MailMessage;
import org.itrunner.heroes.service.mail.MimeMessageMailSender;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

import static org.itrunner.heroes.scheduling.Constants.JOB_REST_URI;
import static org.itrunner.heroes.scheduling.Constants.JOB_STOP_FLAG;

@Slf4j
public class RestJob extends QuartzJobBean {
    @Autowired
    private RestService restService;

    @Autowired
    private MimeMessageMailSender mimeMessageMailSender;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
            String restUri = jobDataMap.getString(JOB_REST_URI);

            ResponseEntity<List> responseEntity = restService.requestForEntity(restUri, HttpMethod.GET, List.class);
            log.info(responseEntity.getBody().toString());

            // set stop flag
            jobExecutionContext.put(JOB_STOP_FLAG, true);
        } catch (Exception e) {
            String message = "Failed to execute rest job: " + e.getMessage();
            log.error(message, e);
            sendMail(message);
        }

    }

    private void sendMail(String text) {
        MailMessage message = new MailMessage();
        message.setTo(new String[]{"jason@163.com"});
        message.setSubject("Rest Job Failed");
        message.setText(text);

        mimeMessageMailSender.sendMail(message);
    }
}
