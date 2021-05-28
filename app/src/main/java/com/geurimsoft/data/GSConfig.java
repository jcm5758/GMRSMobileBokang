package com.geurimsoft.data;

public class GSConfig
{

    // 디버깅용 태그
    public static final String APP_DEBUG = "Geurimsoft";

    // 서버 주소
    //public static final String SERVER_ADDR = "211.253.8.254";
    public static final String SERVER_ADDR = "192.168.0.20";
    //public static final String SERVER_ADDR = "211.221.92.223";

    // API 서버 포트
    public static final int API_SERVER_PORT = 8403;
    //public static final int API_SERVER_PORT = 8200;

    // API 서버 주소
    public static final String API_SERVER_ADDR = "http://" + GSConfig.SERVER_ADDR + ":" + GSConfig.API_SERVER_PORT + "/";

    // Api 재호출 시간
    public static final int API_RECONNECT = 5;

}
