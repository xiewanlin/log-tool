package com.xwl.logtool.convert;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class PrefixSensitiveDataConverter extends SensitiveDataConverter{
    //敏感日志前缀标志
    private static final String SENSITIVE_PREFIX = "!*";

    @Override
    public String convert(ILoggingEvent event) {
        String oriLogMsg = event.getFormattedMessage();
        if(!oriLogMsg.startsWith(SENSITIVE_PREFIX)){
            return oriLogMsg;
        }
        try {
            return super.convert(event);
        }catch (Exception e){
            return oriLogMsg;
        }
    }
}
