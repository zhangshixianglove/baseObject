package com.lezyo.fsm.activity.base;

import android.os.Bundle;


/**
 * 带有网络操作的activity
 */
public abstract class NetWorkActivity extends BaseActivity {
    private NetWorkHelper netWorkHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		netWorkHelper = new NetWorkHelper(this);
		netWorkHelper.onCreate();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		setupData();
	}
	public void setBodyParams (String [] bodyKey, String [] bodyValue) {
		netWorkHelper.setBodyParams(bodyKey, bodyValue);
	}
	public void sendConnection(int method , String url, String[] argsKeys,
			String[] argsValues, int where, boolean showDialog ,Class<?> targerClass) {
		netWorkHelper.sendConnection(method, url, argsKeys, argsValues, where, showDialog, targerClass);
	}
	public void sendConnection(String bizCode,String[] bodyKeys,String[] bodyValues, int where, boolean showDialog, Class<?> targerClass) {
		netWorkHelper.sendConnection(bizCode, bodyKeys, bodyValues, where, showDialog, targerClass);
	}

	// -------------------------需要重写或实现的方法---------------------
	/**
	 * 
	 * 可根据需求重写该方法，优先调用该方法解析
	 * 该方法在线程中调用不要作ui操作
	 * @param where
	 */
	protected Object onParseXml(int where ,String result) {
		return null;
	}
 /**
  * 成功返回结果
  * @param result
  * @param where       
  */
	protected abstract void onSuccess(Object result, int where);
/**
 * 网络连接错误 或服务器返回错误结果时回调改方法
 * @param result
 * @param where
 */
	protected abstract void onFailure(String errMsg, Object result,int where);

	/**
	 * 可以在此方法中配置一些基本数据
	 */
	protected  void setupData(){
	}

    @Override
    protected void onStop() {
    	super.onStop();
    	netWorkHelper.onStop();
    }
}
