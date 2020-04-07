package com.xwl.logtool.configure;

import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.xwl.logtool.handler.HandlerBeanFactory;
import com.xwl.logtool.handler.LogHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfiguration {

  private static final Map<String, Map<String, LogHandler>> KEY_VALUE_HANDLER_MAP = new ConcurrentHashMap<>();

  private static final String LOG_HANDLER_ENABLE = "log.handler.enable";

  private static boolean logInvokeEnable = false;

  public static boolean getLogInvokeEnable() {
    return logInvokeEnable;
  }

  public static Map<String, Map<String, LogHandler>> getKeyValueHandlerMap() {
    return KEY_VALUE_HANDLER_MAP;
  }

  public LogConfiguration() {
    HandlerBeanFactory.getKeyHandlerMap().forEach((k, v) -> {
      String value = ConfigService.getAppConfig().getProperty(k, "");
      if (StringUtils.isNotBlank(value)) {
        String[] valueArray = value.split(",");
        Map<String, LogHandler> valueHandlerMap = new HashMap<>(8);
        for (String val : valueArray) {
          valueHandlerMap.put(val, v);
        }
        KEY_VALUE_HANDLER_MAP.put(k, valueHandlerMap);
      }
    });
    String value = ConfigService.getAppConfig().getProperty(LOG_HANDLER_ENABLE, "");
    if (StringUtils.isNotBlank(value)) {
      logInvokeEnable = Boolean.valueOf(value);
    }
  }

  @ApolloConfigChangeListener
  public void apolloConfigChange(ConfigChangeEvent changeEvent) {
    Set<String> keys = changeEvent.changedKeys();
    keys.forEach(key -> {
      ConfigChange configChange = changeEvent.getChange(key);
      String newValue = configChange.getNewValue();
      LogHandler logHandler = HandlerBeanFactory.getKeyHandler(key);
      if (logHandler != null) {
        Map<String, LogHandler> valueHandlerMap = new HashMap<>(8);
        String[] valueArray = newValue.split(",");
        for (String val : valueArray) {
          valueHandlerMap.put(val, logHandler);
        }
        KEY_VALUE_HANDLER_MAP.put(key, valueHandlerMap);
      }
      if (LOG_HANDLER_ENABLE.equals(key)) {
        logInvokeEnable = Boolean.valueOf(newValue);
      }
    });
  }
}
