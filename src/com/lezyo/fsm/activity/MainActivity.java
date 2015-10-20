package com.lezyo.fsm.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lezyo.fsm.R;
import com.lezyo.fsm.activity.base.BaseEntity;
import com.lezyo.fsm.activity.base.NetWorkActivity;
import com.lezyo.fsm.bean.teacher.LoginResponse;
import com.lezyo.fsm.utils.ToastUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


public class MainActivity extends NetWorkActivity {
	private final static int LOGIN_REQUEST = 1;
	@ViewInject(R.id.userName)
	private EditText userName ;
	@ViewInject(R.id.passWord)
	private EditText passWord ;
	@ViewInject(R.id.context)
	private TextView context ;
	
	@ViewInject(R.id.request)
	private Button buttton ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_main);
	    addBroadReceiver("com.safet.eafe.ce", new BroadcastReceiver(){
	  		@Override
	  		public void onReceive(Context context, Intent intent) {
	  			
	  	    	}
	      });
	}
	
	@OnClick(R.id.request)
	public void doClick(View view) {
		String userName = this.userName.getText().toString();
		String passWord = this.passWord.getText().toString();
		
      this.sendConnection("KJ10008", new String[]{"UserCode","UserPassword","LoginSource"},
    		  new String[]{userName,passWord,"2"}, LOGIN_REQUEST, true, LoginResponse.class);
	}
	
	@Override
	protected void onSuccess(Object result, int where) {
		switch (where) {
		case LOGIN_REQUEST:
			LoginResponse response = (LoginResponse)result;
			System.out.println(response);
			context.setText(response.toString());
			break;
		}
	}
//	@Override
//	protected Object onParseXml(int where, String result) {
//		return super.onParseXml(where, result);
//	}
	@Override
	protected void onFailure(String errMsg, Object result, int where) {
		switch (where) {
		case LOGIN_REQUEST:
			BaseEntity response = (BaseEntity)result;
			System.out.println(response);
			ToastUtil.show(mContext, errMsg);
			break;
		}
	}
}
