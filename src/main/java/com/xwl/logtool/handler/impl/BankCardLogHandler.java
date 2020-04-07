package com.xwl.logtool.handler.impl;

import com.xwl.logtool.handler.LogHandler;
import com.xwl.logtool.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

public class BankCardLogHandler implements LogHandler {
    /**
     * @return java.lang.String
     * @Description 配置中心的key值
     * @Date 2019-6-12 18:12
     * @Param []
     **/
    @Override
    public String apolloKey() {
        return "log.sensitive.bankcard";
    }

    /**
     * @param value
     * @return java.lang.String  脱敏后的值
     * @Description [银行卡号] 前4位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     * @Date 2019-6-12 18:12
     * @Param [value  字段的原始值]
     */
    @Override
    public String hand(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        if(value.length() > 10){
            return StringUtils.left(value, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(value, 4), StringUtils.length(value), "*"), "******"));
        }
        return StringUtil.getMultiStr('*',value.length());
    }
}
