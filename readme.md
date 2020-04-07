## 使用说明
>用户日志脱敏，对用户设定的字段进行脱敏处理
### 配置
pom.xml文件中引入
```xml
<dependency>
    <groupId>xwl-log-tool</groupId>
    <artifactId>log-tool</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
同时检查下面的包是否已经导入，如果没有，也加上
```xml
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>3.7</version>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.2.3</version>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>com.ctrip.framework.apollo</groupId>
  <artifactId>apollo-client</artifactId>
  <version>1.1.2</version>
  <scope>provided</scope>
</dependency>
```

logback.xml文件中加入
```xml
<conversionRule conversionWord="msg" converterClass="com.xwl.logtool.convert.SensitiveDataConverter" />
```
或者
```xml
<conversionRule conversionWord="msg" converterClass="com.xwl.logtool.convert.PrefixSensitiveDataConverter" />
```
建议使用后者，后者只有日志以 '!*'字符串开头的才会进行脱敏。


然后在配置文件中，加入
```properties
##日志脱敏开关
log.handler.enable = true
##需要脱敏的手机号码字段名
log.sensitive.mobile = phoneNum,mobile,phoneNumber,phone,mobileTel,phonecode
##需要脱敏的身份证号字段名
log.sensitive.idcard = idcard,idNo,idNumber
##需要脱敏的银行卡号字段名
log.sensitive.bankcard = bankAC,fundCard,bankcard
##需要脱敏的用户名字段名
log.sensitive.name = clientName,fundCard,localeName,mailName,realname,chineseName,lastName,firstName
##需要脱敏的邮箱字段名
log.sensitive.email = email
```

各个服务根据自己的实际需求，配置不同的字段名即可。

## 支持的日志msg格式
#### json
#### "xxxx字段=什么值"这种格式