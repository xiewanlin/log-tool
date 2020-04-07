package com.xwl.logtool.handler.impl;

import com.xwl.logtool.handler.LogHandler;
import org.apache.commons.lang3.StringUtils;

public class IdCardLogHandler implements LogHandler {
    /**
     * @return java.lang.String
     * @Description 配置中心的key值
     * @Date 2019-6-12 18:12
     * @Param []
     **/
    @Override
    public String apolloKey() {
        return "log.sensitive.idcard";
    }

    /**
     * @param value
     * @return java.lang.String  脱敏后的值
     * @Description [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     * @Date 2019-6-12 18:12
     * @Param [value  字段的原始值]
     */
    @Override
    public String hand(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String num = StringUtils.right(value, 4);
        return StringUtils.leftPad(num, StringUtils.length(value), "*");
    }
}
