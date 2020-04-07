package com.xwl.logtool.handler.impl;

import com.xwl.logtool.handler.LogHandler;
import com.xwl.logtool.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

public class MobileLogHandler implements LogHandler {
    /**
     * @return java.lang.String
     * @Description 配置中心的key值
     * @Date 2019-6-12 18:12
     * @Param []
     **/
    @Override
    public String apolloKey() {
        return "log.sensitive.mobile";
    }

    /**
     * @param value
     * @return java.lang.String
     * @Description [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     * @Date 2019-6-12 18:12
     * @Param [key 字段的key, value  字段的原始值]
     */
    @Override
    public String hand( String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        if(value.length() >= 10){
            return StringUtils.left(value, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(value, 4),StringUtils.length(value), "*"), "***"));
        }
        return StringUtil.getMultiStr('*',value.length());
    }


}
