package com.geurimsoft.bokangnew.conf;

import android.app.Activity;
import android.os.Environment;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AppConfig 
{

	public static ArrayList<Activity> activities = new ArrayList<Activity>();
	

	/**
	 * XML 파일 저장 관련된 정보 
	 */
//	public static String SDCARD_FOLDER 	= 	"BokangNewApp";
//	public static String SAVE_DIR 		= 	 Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SDCARD_FOLDER + "/";
//
	/**
	 * 서버 정보 
	 */

	// 서버 주소
	public static String SERVER_IP = "211.253.8.254";

	// 현재 포트 번호
	public static int SERVER_PORT = 8401;

	// 암호화 키
	public static String SOCKET_KEY = "1234567890123456";
	
	/*
	 * XML Config File 수정본 
	 * 
	 * */
//	public static String XML_DIR = "xml";
//	public static String PLACE_LIST_FILE = "placelist.xml";
//	public static int LAYOUT_TYPE_1 = 1;
//	public static int LAYOUT_TYPE_4 = 4;
	
	
	// 해당일 전체 데이터 (start date, end date)
	public static String SEARCH_URL_1_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>SELECTIONDATE</GCType><GCQuery>";
	public static String SEARCH_URL_1_2 = "</GCQuery></GEURIMSOFT>\n";
	
	// 해당일 고객별 데이터 (start date, end date, customer name)
	public static String SEARCH_URL_2_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>SELECTIONDATEBYCUSTOMER</GCType><GCQuery>\n";
	public static String SEARCH_URL_2_2 = "</GCQuery></GEURIMSOFT>\n";

	// 해당일 품목별 데이터 (start date, end date, product name)
	public static String SEARCH_URL_3_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>SELECTIONDATEBYPRODUCT</GCType><GCQuery>\n";
	public static String SEARCH_URL_3_2 = "</GCQuery></GEURIMSOFT>\n";

	// 해당일 차량별 데이터 (start date, end date, vehicle num)
	public static String SEARCH_URL_4_1= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>SELECTIONDATEBYVEHICLE</GCType><GCQuery>\n";
	public static String SEARCH_URL_4_2 = "</GCQuery></GEURIMSOFT>\n";

	public static int URL_TYPE_SEARCH  	= 1;
	public static int URL_TYPE_CUSTOMER	= 2;
	public static int URL_TYPE_PRODUCT  = 3;
	public static int URL_TYPE_VEHICLE  = 4;
	public static int URL_TYPE_USER  	= 5;

	public static String GET_CUSTOMER_LIST_URL= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>CUSTOMER</GCType></GEURIMSOFT>\n";
	public static String GET_PRODUCT_LIST_URL= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>PRODUCT</GCType></GEURIMSOFT>\n";
	public static String GET_VEHICLE_LIST_URL= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>VEHICLE</GCType></GEURIMSOFT>\n";
	public static String GET_USER_LIST_URL= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>USER</GCType></GEURIMSOFT>\n";
	
	// 통계에서 날짜 변경 시 년, 월 제한 (2020-05-01 추가)
	public static int LIMIT_YEAR = 2020;
	public static int LIMIT_MONTH = 5;
	
	public static int MONTH_STATS_YEAR = 0;
	public static int MONTH_STATS_MONTH = 0;
	
	public static int YEAR_STATS_YEAR = 0;
	
	public static int SITE_YONGIN = 1;
	public static int SITE_GWANGJU = 2;
	public static int SITE_JOOMYUNG = 3;

	public static int MODE_STOCK = 0;
	public static int MODE_RELEASE = 1;
	public static int MODE_PETOSA = 2;

	public static String[] MODE_NAMES = new String[]{"입고", "출고", "토사"};
	
	public static int STATE_AMOUNT = 0;
	public static int STATE_PRICE = 1;

	public static int USER_RIGHT_SIZE = 100;

	public static double moneyDivideNum = 1000;

	public static String changeToCommanString(double value)
	{
		//DecimalFormat formatter = new DecimalFormat("#,###.0");
		DecimalFormat formatter = new DecimalFormat("#,###");
		return formatter.format(value);
	}

	public static String changeToCommanStringWOPoint(double value)
	{
		DecimalFormat formatter = new DecimalFormat("#,###");
		return formatter.format(value);
	}

}

