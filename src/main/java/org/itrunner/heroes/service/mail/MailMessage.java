package org.itrunner.heroes.service.mail;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Date;

/**
 * Models an email message
 */
@Data
public class MailMessage {
    private String from;

    private String name;

    private String replyTo;

    @NotNull
    private String[] to;

    private String[] cc;

    private String[] bcc;

    @NotNull
    private String subject;

    private String text;

    private File[] attachments;

    private Date sentDate;
}
