package com.geurimsoft.bokangnew.data;

import androidx.fragment.app.FragmentActivity;

import com.geurimsoft.bokangnew.apiserver.data.UserInfo;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.view.etc.GwangjuTabActivity;
import com.geurimsoft.bokangnew.view.etc.JoomyungTabActivity;

public class GSConfig
{

    // 디버깅용 태그
    public static final String APP_DEBUG = "Geurimsoft";

    // 서버 주소
    //public static final String SERVER_ADDR = "211.253.8.254";
    //public static final String SERVER_ADDR = "192.168.0.20";
    public static final String SERVER_ADDR = "211.221.92.223";

    // API 서버 포트
    public static final int API_SERVER_PORT = 8403;

    // API 서버 주소
    public static final String API_SERVER_ADDR = "http://" + GSConfig.SERVER_ADDR + ":" + GSConfig.API_SERVER_PORT + "/";

    // Api 재호출 시간
    public static final int API_RECONNECT = 5;

    // 현재 선택한 지점
    public static int CURRENT_BRANCH = 0;

    // 현재 사용자
    public static UserInfo CURRENT_USER = null;

    // Acitivity 리스트 : 지점 수정할 때 수정해야 함
    public static Class[] Activity_LIST = new Class[]{ GwangjuTabActivity.class, com.geurimsoft.bokangnew.view.joomyung.ActivityMain.class};

    public static String LOG_MSG(String className, String funcName)
    {
        return className + "." + funcName + " : ";
    }

    // 현재의 연월일
    public static int DAY_STATS_YEAR = 0;
    public static int DAY_STATS_MONTH = 0;
    public static int DAY_STATS_DAY = 0;

    // 통계에서 날짜 변경 시 년, 월 제한 (2020-05-01 추가)
    public static int LIMIT_YEAR = 2020;
    public static int LIMIT_MONTH = 5;

}