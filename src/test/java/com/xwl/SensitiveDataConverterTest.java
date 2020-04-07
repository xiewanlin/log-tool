package com.xwl;

import com.xwl.logtool.convert.SensitiveDataConverter;
import com.xwl.logtool.handler.HandlerBeanFactory;
import com.xwl.logtool.handler.LogHandler;
import com.xwl.logtool.handler.StartIndex;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SensitiveDataConverterTest {

  public static void main(String[] args) {
    String apolloKey1 = "log.sensitive.mobile";
    String apolloKey2 = "log.sensitive.idcard";
    String apolloKey3 = "log.sensitive.bankcard";
    String apolloKey4 = "log.sensitive.name";
    String[] v1 = {"phoneNum", "mobile", "phoneNumber", "phone", "mobileTel", "phonecode"};
    String[] v2 = {"idcard", "idNo", "idNumber"};
    String[] v3 = {"bankAC", "fundCard", "bankcard"};
    String[] v4 = {"clientName", "fundCard", "localeName", "mailName", "realname", "chineseName", "lastName", "firstName"};

    Map<String, Map<String, LogHandler>> KEY_VALUE_HANDLER_MAP = new ConcurrentHashMap<>();
    Map<String, LogHandler> valueHandlerMap1 = new HashMap<>(8);
    for (String v : v1) {
      valueHandlerMap1.put(v, HandlerBeanFactory.getKeyHandler(apolloKey1));
    }
    KEY_VALUE_HANDLER_MAP.put(apolloKey1, valueHandlerMap1);

    Map<String, LogHandler> valueHandlerMap2 = new HashMap<>(8);
    for (String v : v2) {
      valueHandlerMap2.put(v, HandlerBeanFactory.getKeyHandler(apolloKey2));
    }
    KEY_VALUE_HANDLER_MAP.put(apolloKey2, valueHandlerMap2);

    Map<String, LogHandler> valueHandlerMap3 = new HashMap<>(8);
    for (String v : v3) {
      valueHandlerMap3.put(v, HandlerBeanFactory.getKeyHandler(apolloKey3));
    }
    KEY_VALUE_HANDLER_MAP.put(apolloKey3, valueHandlerMap3);

    Map<String, LogHandler> valueHandlerMap4 = new HashMap<>(8);
    for (String v : v4) {
      valueHandlerMap1.put(v, HandlerBeanFactory.getKeyHandler(apolloKey4));
    }
    KEY_VALUE_HANDLER_MAP.put(apolloKey4, valueHandlerMap4);

    long start = System.currentTimeMillis();
    for (int i = 0; i < 10000; i++) {
      long num = 15652407967L + i;
      String msg = "{\"clientName\":\"谢万霖\",\"phoneNum\":\"" + num
          + "\",\"aaa\":\"12347896\",\"phoneNum\":\"11111111111\",\"idNo\":\"903939384758574\",\"bankcard\":\"6868767764839485768\"}";
      //msg = "phoneNum=" + num + ",idNo=903939384758574";
      StartIndex startIndex = new StartIndex();
      startIndex.setMsg(msg);
      KEY_VALUE_HANDLER_MAP.forEach((k, v) -> SensitiveDataConverter.desensitization(startIndex, v));
      System.out.println(startIndex.getMsg());
    }
    System.out.println("耗时:" + (System.currentTimeMillis() - start) + "毫秒");
  }
}
