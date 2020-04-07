package com.xwl.logtool.handler;

public interface LogHandler {

    /**
    * @Description 配置中心的key值
    * @Date 2019-6-12 18:12
    * @Param []
    * @return java.lang.String
    **/
    String apolloKey();

    /**
    * @Description 处理字段脱敏
    * @Date 2019-6-12 18:12
    * @Param [value  字段的原始值]
    * @return java.lang.String  脱敏后的值
    **/
    String hand(String value);
}
