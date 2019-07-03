package org.itrunner.heroes.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Messages {
    @Resource
    private MessageSource messageSource;

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object[] objects) {
        return messageSource.getMessage(code, objects, LocaleContextHolder.getLocale());
    }
}
