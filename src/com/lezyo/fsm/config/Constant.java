package com.lezyo.fsm.config;

import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.util.LogUtils;



public class Constant {
     //基本访问地址
//	public static String BASE_URL = "http://dev.lezyo.com/index.php/appapi/Process";
//	public static String BASE_URL = "http://uat.lezyo.com/index.php/appapi/Process";         //UAT
//	public static String BASE_URL = "http://staging.lezyo.com/index.php/appapi/Process";     //staging
//	public static String BASE_URL = "http://test.lezyo.com/index.php/appapi/Process";        //test
	public static String BASE_URL = "http://113.200.56.12:7001/Service";     //apptest
//    public static String BASE_URL = "http://www.lezyo.com/index.php/appapi/Process";       //BASE
	//屏幕宽高
	public static int screenH;
	public static int screenW;
	//log开关
	/**以后log输出以(LogUtils.*)方式输出**/
	public static boolean LOG_OPEN = true;
	/**是否打印友盟的log*/
	public static boolean UMENG_LOG = true;
	/**数据库名字*/
	public static final String DATA_BASE_NAME = "Lezyo.db";
	public static final String TABLE_CACHE = "cacheTab";
	/**缓存过期时间 天为单位*/
	public static final int OUT_TIME_DAY = 10;
	/**缓存图片路径*/
	public static final String CACHE_PATH = "lezyo_image";
	public static final String CACHE_BIG_PATH = "lezyo_other_image";

	public static void logOpen () {
		LogUtils.allowD = true;
		LogUtils.allowE = true;
		LogUtils.allowI = true;
		LogUtils.allowV = true;
		LogUtils.allowW = true;
		LogUtils.allowWtf = true;
		VolleyLog.DEBUG = true;
	}
	public static void logClose () {
		LogUtils.allowD = false;
		LogUtils.allowE = false;
		LogUtils.allowI = false;
		LogUtils.allowV = false;
		LogUtils.allowW = false;
		LogUtils.allowWtf = false;
		VolleyLog.DEBUG = false;
	}

}
