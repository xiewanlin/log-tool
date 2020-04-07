package com.xwl.logtool.convert;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.xwl.logtool.configure.LogConfiguration;
import com.xwl.logtool.handler.LogHandler;
import com.xwl.logtool.handler.StartIndex;
import java.util.Map;

public class SensitiveDataConverter extends MessageConverter {

  private static final int MAX_LENGTH = 2048;

  @Override
  public String convert(ILoggingEvent event) {
    String oriLogMsg = event.getFormattedMessage();
    if (oriLogMsg.length() > MAX_LENGTH) {
      oriLogMsg = oriLogMsg.substring(0, MAX_LENGTH);
    }
    return LogConfiguration.getLogInvokeEnable() ? this.invokeMsg(oriLogMsg) : oriLogMsg;
  }


  /**
   * 处理日志字符串，返回脱敏后的字符串
   * @return
   */
  public String invokeMsg(final String oriMsg) {
    Map<String, Map<String, LogHandler>> keyValueHandlerMap = LogConfiguration.getKeyValueHandlerMap();
    StartIndex startIndex = new StartIndex();
    startIndex.setMsg(oriMsg);
    keyValueHandlerMap.forEach((k, v) -> desensitization(startIndex, v));
    return startIndex.getMsg();
  }


  public static String desensitization(StartIndex startIndex, Map<String, LogHandler> map) {
    int equalsIndex = startIndex.getMsg().indexOf("=");
    int colonIndex = startIndex.getMsg().indexOf(":");
    map.forEach((k, v) -> {
      int index = startIndex.getMsg().indexOf(k);
      if (index != -1) {
        startIndex.setKey(k);
        startIndex.setIndex(index);
        startIndex.setLogHandler(v);
        if (equalsIndex > -1) {
          doEqualsInvokeMsg(startIndex);
        } else if (colonIndex > -1) {
          doJsonInvokeMsg(startIndex);
        }
      }
    });
    return startIndex.getMsg();
  }

  private static void doJsonInvokeMsg(StartIndex startIndex) {
    String key = startIndex.getKey();
    int index = startIndex.getIndex();
    String msg = startIndex.getMsg();
    int equalsIndex = msg.indexOf(":", index);
    if (equalsIndex > -1) {
      int valueStart = equalsIndex + 1;
      int valueEnd = valueStart;
      do {
        char nextCh = msg.charAt(valueEnd);
        if (nextCh == ',' || nextCh == '}') {
          break;
        }
        valueEnd += 1;
      } while (valueEnd < msg.length());
      String value = msg.substring(valueStart, valueEnd);
      String replaceValue = startIndex.getLogHandler().hand(value.replaceAll("\"", "").trim());
      startIndex.setMsg(msg.substring(0, valueStart) + replaceValue + msg.substring(valueEnd));
      int nextIndex = msg.indexOf(key, valueEnd);
      if (nextIndex > -1) {
        int t = value.length() - replaceValue.length();
        startIndex.setIndex(nextIndex - t);
        doJsonInvokeMsg(startIndex);
      }
    }
  }

  private static void doEqualsInvokeMsg(StartIndex startIndex) {
    String key = startIndex.getKey();
    int index = startIndex.getIndex();
    String msg = startIndex.getMsg();

    int equalsIndex = msg.indexOf("=", index);
    if (equalsIndex > -1) {
      int valueStart = equalsIndex + 1;
      int valueEnd = valueStart;
      do {
        char nextCh = msg.charAt(valueEnd);
        if (nextCh == ',') {
          break;
        }
        valueEnd += 1;
      } while (valueEnd < msg.length());
      String value = msg.substring(valueStart, valueEnd);
      String replaceValue = startIndex.getLogHandler().hand(value.replaceAll("\"", "").trim());
      startIndex.setMsg(msg.substring(0, valueStart) + replaceValue + msg.substring(valueEnd));
      int nextIndex = msg.indexOf(key, valueEnd);
      if (nextIndex > -1) {
        int t = value.length() - replaceValue.length();
        startIndex.setIndex(nextIndex - t);
        doJsonInvokeMsg(startIndex);
      }
    }
  }

}
