package com.lezyo.fsm.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;

public class CommonUtils {
	
	private static Random random = new Random();
	/**
	 * 根据手机分辨率从dp转成px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static  int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
	  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
	public static  int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f)-15;  
    }  
	/**
	 * 判断是否有网络连接
	 */
	public static boolean isNetworkConnected(Context context) {  
	    if (context != null) {  
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	        if (mNetworkInfo != null) {  
	            return mNetworkInfo.isAvailable();  
	        }  
	    }  
	    return false;  
	}
	
    /**  
     * 判断当前网络是否是wifi网络  
     *   
     * @param context  
     * @return boolean  
     */   
    public static boolean isWifi(Context context) {   
        ConnectivityManager connectivityManager = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();   
        if (activeNetInfo != null   
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {   
            return true;   
        }   
        return false;   
    }   
   
    /**  
     * 判断当前网络是否是3G网络  
     *   
GPRS       2G(2.5) General Packet Radia Service 114kbps
EDGE       2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps

CDMA     2G 电信 Code Division Multiple Access 码分多址
1xRTT      2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
IDEN      2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）

EVDO_0   3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
EVDO_A  3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
UMTS      3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
HSDPA    3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps 
HSUPA    3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps

HSPA      3G (分HSDPA,HSUPA) High Speed Packet Access 
EVDO_B 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
EHRPD  3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
HSPAP  3G HSPAP 比 HSDPA 快些

LTE        4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G 

     * @param context  
     * @return boolean  
     */   
    public static boolean is3G(Context context) {   
    	TelephonyManager telephoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
    	final int type = telephoneManager.getNetworkType(); 
    	//联通3g
    	if (type == TelephonyManager.NETWORK_TYPE_UMTS  
                || type == TelephonyManager.NETWORK_TYPE_HSDPA
                ||type == TelephonyManager.NETWORK_TYPE_HSUPA) {
    		return true;
    	}
    	//电信3g
    	else if (type==TelephonyManager.NETWORK_TYPE_EVDO_0  
                ||type==TelephonyManager.NETWORK_TYPE_EVDO_A) {
    		return true;
    	}
    	//其他3g网络
    	else if ( type == TelephonyManager.NETWORK_TYPE_HSPA
    			||type==TelephonyManager.NETWORK_TYPE_EVDO_B
    			||type==TelephonyManager.NETWORK_TYPE_EHRPD
    			||type==TelephonyManager.NETWORK_TYPE_HSPAP) {
    		return true;
    	}
    	//4g网络
    	else	if (type == TelephonyManager.NETWORK_TYPE_LTE) {
    		return true;
    	}
    	return false;
    }   
    /**
     *获取IMEI
     * @param context
     * @return
     */
    public static String getIMEI(Context context){
    	TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getDeviceId();
    }
	/**
	 * 获取手机IMSI
	 * @return
	 */
	public static final String createIMSI() {
		StringBuffer strbuf = new StringBuffer();
		for(int i = 0; i < 15; i++) strbuf.append(random.nextInt(10));
		return strbuf.toString();
	}
	/**
	 * 返回电话的IMEI:手机的唯一标识码
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		String imei = "";
		TelephonyManager tm =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			imei = tm.getDeviceId();
			if(isEmpty(imei)) {
				imei = createIMSI();
			}
		}
		return imei;
	}

	/**
	 * 手机制造商
	 * @return
	 */
	public static String getProduct(){
		
		return android.os.Build.PRODUCT; 
	}
	/**
	 * 手机系统版本
	 * @return
	 */
	public static String getOsVersion(){
		return android.os.Build.VERSION.RELEASE;
	}
	/**
	 * 获取当前的版本号
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context){
		String version = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	      return version;
	}
	/**
	 * 通过Wifi获取MAC ADDRESS作为DEVICE ID
	 * 缺点：如果Wifi关闭的时候，硬件设备可能无法返回MAC ADDRESS.。
	 * @return
	 */
	public static String getMacAddress() {
	    String macSerial = null;
	    String str = "";
	    try {
	        Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
	        InputStreamReader ir = new InputStreamReader(pp.getInputStream());
	        LineNumberReader input = new LineNumberReader(ir);

	        for (; null != str;) {
	            str = input.readLine();
	            if (str != null) {
	                macSerial = str.trim();// 去空格
	                break;
	            }
	        }
	    } catch (IOException ex) {
	        // 赋予默认值
	        ex.printStackTrace();
	    }
	    return macSerial;
	}
	/**
	 * 判断是否为空
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return (s == null || "".equals(s) || "null".equals(s.toLowerCase()));
	}
	
	/**
	 * 得到手机屏幕的宽
	 * @param mActivity 
	 * @return
	 */
	public static int getScreenWidth(Activity mActivity){
		DisplayMetrics dm = new DisplayMetrics();  
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);  
		return dm.widthPixels;
	}
	/**
	 * 得到手机屏幕的高
	 * @param act
	 * @return
	 */
	public static int getScreenHeight(Activity mActivity){
		DisplayMetrics dm = new DisplayMetrics();  
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);  
		return dm.heightPixels;
	}
	
	/**
	 * 取得文件大小
	 * @param f
	 * @return
	 * @throws Exception
	 */
	 public static long getFileSizes(File f) throws Exception{
	        long s=0;
	        if (f.exists()) {
	            FileInputStream fis = null;
	            fis = new FileInputStream(f);
	            s= fis.available();
	        } else {
	            f.createNewFile();
	            System.out.println("文件不存在");
	        }
	        return s;
	    }
	 	/**
	 	 *  递归  //取得文件夹大小
	 	 * @param f
	 	 * @return
	 	 * @throws Exception
	 	 */
	    public static long getFileFolderSize(File f) throws Exception {
	        long size = 0;
	        File flist[] = f.listFiles();
	        for (int i = 0; i < flist.length; i++){
	            if (flist[i].isDirectory()){
	            	size = size + getFileFolderSize(flist[i]);
	            } else{
	            	size = size + flist[i].length();
	            }
	        }
	        return size;
	    }
	    /**
	     * 转换文件大小
	     * @param fileS
	     * @return
	     */
	    public static String formetFileSize(long fileS) {
	        DecimalFormat df = new DecimalFormat("#.##");
	        String fileSizeString = "";
	        if(fileS == 0 || fileS < 350) return "0M";
	        if (fileS < 1024) {
	            fileSizeString = df.format((double) fileS) + "B";
	        } else if (fileS < 1048576) {
	            fileSizeString = df.format((double) fileS / 1024) + "K";
	        } else if (fileS < 1073741824) {
	            fileSizeString = df.format((double) fileS / 1048576) + "M";
	        } else {
	            fileSizeString = df.format((double) fileS / 1073741824) + "G";
	        }
	        return fileSizeString;
	    }

	 /** 根据key获取metadata中的值
	 * @param context
	 * @param channelKey
	 * @return
	 */
    public static int getMetaDataIntValue(Context context , String channelKey) {
    	int msg = 0;
		try {
			 ApplicationInfo appInfo = context.getPackageManager() .getApplicationInfo(context.getPackageName(),
			  PackageManager.GET_META_DATA);
              msg=appInfo.metaData.getInt(channelKey);
		 } catch (NameNotFoundException e) {
			e.printStackTrace();
		}
         return msg;
    }
	 /** 
	  * 根据key获取metadata中的值
	 * @param context
	 * @param channelKey
	 * @return
	 */
   public static String getMetaDataStringValue(Context context , String channelKey) {
   	String msg = "";
		try {
			 ApplicationInfo appInfo = context.getPackageManager() .getApplicationInfo(context.getPackageName(),
			  PackageManager.GET_META_DATA);
             msg=appInfo.metaData.getString(channelKey);
		 } catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        return msg;
   }
   /**
    * 判断字符串是否为纯数字
    * @param str
    * @return
    */
   public static boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}
   
   /**
    * is has sd
    * @return
    */
   public static boolean isSDCard(){
	   return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
   }
	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent;
	}
	/**
	 * 返回保存图片名字
	 * @return
	 */
	public static String getPicName() {
	    SimpleDateFormat fat = new SimpleDateFormat("yyyyMMdd_hhmmss");
	    return fat.format(new Date())+".jpg";
	}
	
	
	public static Bitmap convertViewToBitmap(View view){
	   view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	   view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
	   view.buildDrawingCache();
	   Bitmap bitmap = view.getDrawingCache();

	   return bitmap;
	}
	
	public static long getTimelong(String str) {
		  SimpleDateFormat formatter = new SimpleDateFormat(str);
		  Calendar mCalendar = formatter.getCalendar();
		  return mCalendar.getTimeInMillis();
	}
	/**
	 * 判断字符串是否超过 指定的长度
	 * @param name
	 * @param length
	 * @return
	 */
	public static boolean isTooLong(String name,int length){
		String gbkStr = null;
		if(name.contains(" ")) name = name.replace(" ", "");
		if(name.contains("."))  name = name.replace(".", "");
		if(name.contains("·"))  name = name.replace("·", "");
		if(name.contains("。"))  name = name.replace("。", "");
		 byte[] b = name.getBytes();
		 try {
			 //转为GBK编码
			gbkStr = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 if(!isEmpty(gbkStr)){
			 b = gbkStr.getBytes();
		 }
		 
		 if(b.length <= length){
			 return true;
		 }
		return false;
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	/**
	 * 转bitmap -> drawable
	 */
	public static Drawable convertBitmap2Drawable(Context context,Bitmap bitmap) {
		BitmapDrawable td = new BitmapDrawable(context.getResources(),bitmap);
		return td;
	}
    //获取渠道
    public static String getChannel(Context context , String channelKey) {
    	String msg = null;
		try {
			 ApplicationInfo appInfo = context.getPackageManager() .getApplicationInfo(context.getPackageName(),
			  PackageManager.GET_META_DATA);
              msg=appInfo.metaData.getString(channelKey);
		 } catch (NameNotFoundException e) {
			e.printStackTrace();
		}
         return msg;
    }
}
