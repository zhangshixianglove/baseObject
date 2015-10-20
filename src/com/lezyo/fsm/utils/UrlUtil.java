package com.lezyo.fsm.utils;

import android.text.TextUtils;

public class UrlUtil {
	/**去掉url中的sign*/
	public static String getCacheKey (String url) {
		if ( TextUtils.isEmpty(url) || !url.contains("sign=")) {
			return url;
		}
		int startInt = url.indexOf("sign=");
		int endInt = url.indexOf("&", startInt);
		StringBuffer buff = new StringBuffer ();
		String startStr = url.substring(0, startInt);
		String endStr = url.substring(endInt + 1);
		url = buff.append(startStr).append(endStr).toString();
		if (!url.contains("&t=")) {
			return url;
		}
		 startInt = url.indexOf("&t=");
		 endInt = url.indexOf("&", startInt+1);
		 buff = new StringBuffer ();
		 startStr = url.substring(0, startInt);
		 endStr = url.substring(endInt);
		 return buff.append(startStr).append(endStr).toString();
	}

}
