package com.qckj.dabei.app;

/**
 * 系统配置文件
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public class SystemConfig {

    static final String serverUrlTest = "http://192.168.1.112:8888/DaBei";

    static final String serverUrl = "https://www.dabeiinfo.com";

    /**
     * 获取签约api地址
     *
     * @param routeUrl 路由字段，'/'开头
     * @return api地址
     */
    public static String getServerUrlTest(String routeUrl) {
        return serverUrlTest + routeUrl;
    }

    /**
     * 获取签约api地址
     *
     * @param routeUrl 路由字段，'/'开头
     * @return api地址
     */
    public static String getServerUrl(String routeUrl) {
        return serverUrl + routeUrl;
    }

}
