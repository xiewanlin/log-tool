package com.xwl.logtool.utils;

import java.util.Arrays;

public class StringUtil {
    /**
    * @Description 生成相同重复字符的串
    * @Date 2019-8-22 14:28
    * @Param [element, num]
    * @return java.lang.String
    **/
    public static String getMultiStr(char element,int num){
        char[] chars = new char[num];
        Arrays.fill(chars, element);
        return new String(chars);
    }

    public static void main(String[] args) {
        System.out.println(getMultiStr('*',6));
    }
}
