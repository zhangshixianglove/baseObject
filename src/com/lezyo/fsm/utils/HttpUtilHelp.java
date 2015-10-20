package com.lezyo.fsm.utils;

import com.lezyo.fsm.RentApplication;
import com.lidroid.xutils.HttpUtils;

public class HttpUtilHelp {
	 private static HttpUtils httpUtils;
      private HttpUtilHelp() {
      }
      public static HttpUtils getHttpUtils () {
    	  if (httpUtils == null) {
    		  httpUtils = new HttpUtils();
    	  }
    	httpUtils.configTimeout(30*1000);
    	httpUtils.configUserAgent(RentApplication.userAgent);
    	  return httpUtils;
      }
}
