package com.xwl.logtool.handler.impl;

import com.xwl.logtool.handler.LogHandler;
import com.xwl.logtool.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

public class EmailLogHandler implements LogHandler {
    /**
     * @return java.lang.String
     * @Description 配置中心的key值
     * @Date 2019-6-12 18:12
     * @Param []
     **/
    @Override
    public String apolloKey() {
        return "log.sensitive.email";
    }

    /**
     * @param value
     * @return java.lang.String  脱敏后的值
     * @Description 处理邮箱字段脱敏
     * @Date 2019-6-12 18:12
     * @Param [value  字段的原始值]
     */
    @Override
    public String hand(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        int index = StringUtils.indexOf(value, "@");
        if (index < 1){
            return value;
        }
        else{
            String emalPre = StringUtils.rightPad(StringUtils.left(value, 1), index, "*");
            int preLength = StringUtils.length(emalPre);
            if(preLength <= 2){
                emalPre = StringUtil.getMultiStr('*',preLength);
            }
            return emalPre.concat(StringUtils.mid(value, index, StringUtils.length(value)));
        }
    }

    public static void main(String[] args) {
        System.out.println(new MobileLogHandler().hand("1234567891"));
    }
}
