package com.zhulang.basic.utils.common;

import java.net.InetAddress;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class Environment {
    public static String IP;
    public static String APP_ID;
    public static String ENV;

    public Environment() {
    }

    static {
        try {
            APP_ID = System.getProperty("APPID");
            ENV = System.getProperty("env");
            IP = InetAddress.getLocalHost().getHostAddress();
        } catch (Throwable var1) {
        }

    }
}
