package com.xwl.logtool.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HandlerBeanFactory {

  private static final Map<String, LogHandler> KEY_HANDLER_MAP = new ConcurrentHashMap<>(8);

  static {
    try {
      String packageName = HandlerBeanFactory.class.getPackage().getName() + ".impl";
      Set<Class<?>> classSet = getClassSet(packageName);
      for (Class<?> clazz : classSet) {
        System.out.println("本次class:" + clazz.getName());
        if(clazz.isInterface()){
          continue;
        }

        Object o = clazz.newInstance();
        if(o instanceof LogHandler){
          LogHandler obj = (LogHandler) o;
          KEY_HANDLER_MAP.put(obj.apolloKey(), obj);
          System.out.println(obj.getClass().getName() + "已经加载...");
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  public static Map<String, LogHandler> getKeyHandlerMap() {
    return KEY_HANDLER_MAP;
  }


  public static LogHandler getKeyHandler(String key) {
    return KEY_HANDLER_MAP.get(key);
  }

  public static void addKeyHandler(LogHandler logHandler) {
    KEY_HANDLER_MAP.put(logHandler.apolloKey(), logHandler);
  }

  @Deprecated
  private static List<String> getHandlerClassNames() throws Exception {
    String handlerPath = HandlerBeanFactory.class.getResource("").getPath();
    System.out.println("handlerPath = " + handlerPath);
    if (handlerPath.indexOf("jar") > -1) {
      handlerPath = handlerPath.replace("file:","jar:");
      String jarFilePath = handlerPath.substring(0,handlerPath.lastIndexOf("!")).replace("!","");
      System.out.println("jarFilePath = " + jarFilePath);
      JarFile jarFile = new JarFile(jarFilePath);
      Enumeration<JarEntry> entries = jarFile.entries();
      List<String> handlerClassNames = new ArrayList<>();
      while (entries.hasMoreElements()) {
        JarEntry jarEntry = entries.nextElement();
        String entryName = jarEntry.getName();
        if (entryName.indexOf("impl") > -1 && entryName.indexOf(".class") > -1) {
          handlerClassNames.add(entryName.substring(entryName.lastIndexOf("/") + 1));
        }
      }
      return handlerClassNames;
    }
    File packageDir = new File(handlerPath + "impl");
    if (packageDir.isDirectory()) {
      return Arrays.asList(packageDir.list());
    } else {
      return null;
    }
  }

  /**
   * 获取类加载器
   * @return
   */
  public static ClassLoader getClassloader(){
    return Thread.currentThread().getContextClassLoader();
  }

  /**
   * 获取指定包下的类集合
   * @param packageName
   * @return
   */
  public static Set<Class<?>> getClassSet(String packageName){

    Set<Class<?>> classSet = new HashSet<Class<?>>();

    try {
      Enumeration<URL> urls = getClassloader().getResources(packageName.replace(".","/"));
      while (urls.hasMoreElements()){
        URL url = urls.nextElement();

        Assert.notNull(url,"url is null!");
        String protocol = url.getProtocol();
        if("file".equals(protocol)){
          String path = url.getPath().replaceAll("%20"," ");
          addClass(classSet,path,packageName);
        }else if("jar".equals(protocol)){
          JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
          Assert.notNull(jarURLConnection,"jar url is null!");

          JarFile jarFile = jarURLConnection.getJarFile();
          Assert.notNull(jarFile,"jarfile is null!");

          Enumeration<JarEntry> jarEntries = jarFile.entries();
          while (jarEntries.hasMoreElements()){
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();
            if(jarEntryName.endsWith(".class")){
              String className = jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/",".");
              doAddClass(classSet,className);
            }
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return classSet;
  }

  /**
   * 解析基础包下的class
   *
   * @param classSet
   * @param path
   * @param packageName
   */
  public static void addClass(Set<Class<?>> classSet,String path,String packageName){
    File[] files = new File(path).listFiles(new FileFilter(){
      @Override
      public boolean accept(File file) {
        return (file.isFile()&& file.getName().endsWith(".class")) || file.isDirectory();
      }
    });
    for (File file : files){
      String fileName = file.getName();
      if(file.isFile()){
        String className = fileName.substring(0,fileName.lastIndexOf("."));
        if(StringUtils.isNotEmpty(packageName)){
          className = packageName + "." +className;
        }
        doAddClass(classSet,className);
      }else {
        String subPackagePath = fileName;
        if(StringUtils.isNotEmpty(path)){
          subPackagePath = path + "/" +subPackagePath;
        }
        String subPackageName = fileName;
        if(StringUtils.isNotEmpty(packageName)){
          subPackageName = packageName + "." +subPackageName;
        }
        addClass(classSet,subPackagePath,subPackageName);
      }
    }
  }

  private static void doAddClass(Set<Class<?>> classSet, String className) {
    Class<?> clas = loadClass(className,false);
    classSet.add(clas);
  }

  /**
   * 加载类
   *
   * @param className
   * @param isInitialized
   * @return
   */
  public static Class<?> loadClass(String className,boolean isInitialized){
    Class<?> clazz = null;
    try {
      clazz = Class.forName(className,isInitialized,getClassloader());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return clazz;
  }
}
