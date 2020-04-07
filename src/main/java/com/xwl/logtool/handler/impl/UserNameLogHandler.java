package com.xwl.logtool.handler.impl;

import com.xwl.logtool.handler.LogHandler;
import org.apache.commons.lang3.StringUtils;

public class UserNameLogHandler implements LogHandler {
    /**
     * @return java.lang.String
     * @Description 配置中心的key值
     * @Date 2019-6-12 18:12
     * @Param []
     **/
    @Override
    public String apolloKey() {
        return "log.sensitive.name";
    }

    /**
     * @param value
     * @return java.lang.String  脱敏后的值
     * @Description [姓名] 只显示第一个汉字，其他隐藏为星号<例子：谢**>
     * @Date 2019-6-12 18:12
     * @Param [value  字段的原始值]
     */
    @Override
    public String hand(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String name = StringUtils.left(value, 1);
        return StringUtils.rightPad(name, StringUtils.length(value), "*");
    }
}
