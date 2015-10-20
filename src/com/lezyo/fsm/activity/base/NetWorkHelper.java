package com.lezyo.fsm.activity.base;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.lezyo.fsm.RentApplication;
import com.lezyo.fsm.config.Constant;
import com.lezyo.fsm.utils.CommonUtils;
import com.lezyo.travel.customview.CustomProgressDialog;
import com.lidroid.xutils.util.LogUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
/**
 * 网络请求帮助类
 */
public class NetWorkHelper {
	private RequestQueue requestQuerue;
	private CustomProgressDialog progressDialog;
	private Map<Integer , Request<BaseEntity>> requestStack;
    private Map <String,String> bodyParams;
    private Activity activity;
    private BaseFragment fragment;
    public NetWorkHelper(BaseActivity activity) {
    	this.activity = activity;
    }
    public NetWorkHelper(Activity activity , BaseFragment fragment){
    	this.activity = activity;
    	this.fragment = fragment;
    }
    public void onCreate () {
		requestQuerue =  Volley.newRequestQueue( activity );
		progressDialog = new CustomProgressDialog( activity );
		progressDialog.setMessage("载入中...");
		requestStack = new HashMap<Integer , Request<BaseEntity>>();
    }
	/**
	 * 当为post请求是 调用改方法设置body体 参数,此方法必须在sendConnection 方法前调用
	 * @param bodyKey
	 * @param bodyValue
	 */
	public void setBodyParams (String [] bodyKey, String [] bodyValue) {
		if (bodyKey.length != bodyValue.length) {
			throw new IllegalArgumentException("check your BodyParams key or value length!");
		}
		bodyParams = new HashMap<String, String>();
		String bodyParam ="body参数：";
		for (int i = 0; i<bodyKey.length; i++) {
			bodyParam += bodyKey[i]+"=" +bodyValue[i] +"&";
			bodyParams.put(bodyKey[i], bodyValue[i]);
		}
		if (bodyParam.lastIndexOf("&") >0) {
			bodyParam = bodyParam.substring(0, bodyParam.length()-1);
		}
	
	}
	
	/**
	 * 发送http请求
	 * 
	 * @param url
	 * @param argsKeys  url参数key
	 * @param argsValues url参数值
	 * @param where
	 * @param showDialog   是否显示进度条
	 */
	public void sendConnection(int method , String url, String[] argsKeys,
			String[] argsValues, int where, boolean showDialog ,Class<?> targerClass) {
		if (!CommonUtils.isNetworkConnected(activity)) {
			progressDialog.dismiss();
		}
		if (argsKeys.length != argsValues.length) {
			throw new IllegalArgumentException("check your Params key or value length!");
		}
		StringBuffer queryParam = new StringBuffer();
		for (int i = 0; i<argsKeys.length; i++) {
			queryParam.append(argsKeys[i]+"=" +argsValues[i] +"&");
		}
		if (queryParam.length()> 0 && "&".equals(queryParam.charAt(queryParam.length() -1))){
			queryParam.deleteCharAt(queryParam.length() -1);
			url += "?"+queryParam.toString();
		}
		RequestSuccessListener<BaseEntity> succeessLietener = new RequestSuccessListener<BaseEntity>(where);
		RequestErrorListener errorLietener = new RequestErrorListener(where);
		HttpCallBack<BaseEntity> httpCallback = new HttpCallBack<BaseEntity>
		( url ,succeessLietener , errorLietener ,where , method, bodyParams,targerClass);
		
		Request<BaseEntity> request = requestQuerue.add(httpCallback);
		requestStack.put(where, request);
		if (showDialog) {
		showProgressDialog();
		}
	}
	
	/**
	 * 封装后台请求方式，尽可能的简化了请求
	 * @param bizCode  接口业务代码
	 * @param bodyKeys  附加参数
	 * @param bodyValues
	 * @param where      
	 * @param showDialog   
	 * @param targerClass
	 */
	public void sendConnection(String bizCode,String[] bodyKeys,String[] bodyValues, int where, boolean showDialog, Class<?> targerClass) {
		StringBuffer requestParams = new StringBuffer();
		for ( int i = 0; i < bodyKeys.length; i ++ ) {
			String requestKey = bodyKeys[i];
			String requestValue = bodyValues[i];
			requestParams.append("<"+requestKey+">" + requestValue + "</"+requestKey+">");
		}
		String request = "<Root><BizCode>"+bizCode+"</BizCode><ClientType>android</ClientType>"
				+ "<ClientOS>android 4.1.2</ClientOS><ClientIP>10.38.1.110</ClientIP>" + "<Award></Award>"
				+ "<SessionId></SessionId>" + "<SvcContent>" + "<![CDATA[" + "<Request>"
				+ requestParams.toString()+ "</Request>  ]]></SvcContent></Root>";
		LogUtils.e(request);
		setBodyParams(new String[]{"xmlmsg","type","ps"}, new String[]{request,"xml","0"});
		sendConnection(Method.POST , Constant.BASE_URL,  new String[]{}, new String[]{}, where, showDialog ,targerClass);
	}
	
	// -------------------------------------------------
    private class HttpCallBack<T extends BaseEntity> extends BaseXmlRequest<T>{
    	private Class<?> targerClass;
    	private int where;
		private Map<String,String> paramsMap;
		public HttpCallBack(String url, Listener<T> listener, ErrorListener errorListener, int where, int method,Map<String,String> paramsMap,Class<?> targerClass) {
			super(method, url, listener, errorListener);
			this.where = where;
			this.targerClass = targerClass;
			if ( method == Method.POST) {
			  this.paramsMap = paramsMap;
			}
			else {
			  this.paramsMap = null;
			}
		}
		@SuppressWarnings("unchecked")
		@Override
		protected Response<T> parseNetworkResponse(NetworkResponse response) {
	        String parsed;
	        try {
	            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	        } catch (UnsupportedEncodingException e) {
	            parsed = new String(response.data);
	        }
	        LogUtils.e(parsed);
			T resultObj = null;
			if ( fragment != null ) {
				resultObj = (T)((NetWorkFragment)fragment).onParseXml( where , parsed);
			}else{
				resultObj = (T)((NetWorkActivity)activity).onParseXml( where , parsed);
			}
	        if ( resultObj == null ) {
	            XStream xStream = new XStream(new Dom4JDriver());
		        xStream.alias("Response", targerClass);
		        resultObj = (T) xStream.fromXML(parsed);
	        }
	        return Response.success(resultObj, HttpHeaderParser.parseCacheHeaders(response));
	    }
		@Override
		protected Map<String, String> getParams() throws AuthFailureError {
			return paramsMap;
		}
		
		@Override
		public Map<String, String> getHeaders() throws AuthFailureError {
			 Map<String, String> headerMap = new HashMap<String, String>();
			  headerMap.put("User-Agent", RentApplication.userAgent);
			  return headerMap;
		}
		
    }
    
    private class RequestErrorListener implements ErrorListener{
    	private int what;
    	public RequestErrorListener( int what ){
    		this.what = what;
    	}
		@Override
		public void onErrorResponse(VolleyError error) {
			dismissProgressDialog( what );
			LogUtils.e(error.getMessage() ,error.getCause());
			if ( fragment != null ) {
				((NetWorkFragment)fragment).onFailure(error.getMessage(), null, what);
			}
			else {
				((NetWorkActivity)activity).onFailure(error.getMessage(), null, what);
			}
			
		}
    }
    private class RequestSuccessListener<T extends BaseEntity> implements Listener<T>{
    	private int what;
    	public RequestSuccessListener( int what ){
    		this.what = what;
    	}
		@Override
		public void onResponse(T response) {
			dismissProgressDialog( what );
			String repCode = response.getRspCode();
			if ( "0000".equals(repCode)) {
				if ( fragment != null ) {
					((NetWorkFragment)fragment).onSuccess(response,what);
				}
				else {
					((NetWorkActivity)activity).onSuccess(response,what);
				}
			}
			else {
				if ( fragment != null ) {
					((NetWorkFragment)fragment).onFailure(response.getRspInfo(), response, what);
				}
				else {
					((NetWorkActivity)activity).	onFailure(response.getRspInfo(), response, what);
				}
			}
		}
    }
    /**
     * 隐藏加载对话框
     */
	public  void dismissProgressDialog( int where ) {
		if (requestStack.size() > 1) {
			requestStack.remove(where);
		} 
		else {
			if ( !activity.isFinishing()&&progressDialog.isShowing() ) {
			progressDialog.dismiss();
			}
			requestStack.clear();
		}
	}
	/**
	 * 显示加载对话框
	 */
     public void showProgressDialog() {
   	  if ( !progressDialog.isShowing() && !activity.isFinishing()) {
			progressDialog.show();
   	  }
	}
    public void onStop() {
    	for (Entry<Integer, Request<BaseEntity>> requestBean:  requestStack.entrySet()) {
    		Request<BaseEntity> noRequest = requestStack.get(requestBean.getKey());
    		noRequest.cancel();
    	}
    }
    
	/**
	 * 设置加载对话框是否可以被取消
	 * @param canable
	 */
    public void setProgressCancelable(boolean canable){
    	progressDialog.setCancelable(canable);
    }
    /**
     * 设置加载对话框提示的文字
     * @param message
     */
	public void setProgressDialogMessage (String message) {
		progressDialog.setMessage(message);
	}
}
