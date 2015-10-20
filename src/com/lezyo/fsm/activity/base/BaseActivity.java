package com.lezyo.fsm.activity.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lezyo.fsm.R;
import com.lezyo.fsm.RentApplication;
import com.lezyo.fsm.config.Constant;
import com.lezyo.travel.customview.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 基类activity 每一个acitivty都要继承该类
 * @date 2013-12-12
 */
public abstract class BaseActivity extends Activity {
//	protected final String TAG = getClass().getSimpleName();
	protected Context mContext;
	//通用对话框子类可以直接使用
	protected CustomDialog dialog;
	private List<BroadcastReceiver> receiverList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Constant.screenW = metrics.widthPixels;
		Constant.screenH = metrics.heightPixels;
		RentApplication.getInstance().addActivity(this);
		mContext = this;
		dialog = new CustomDialog( mContext );
		//		getWindow().setWindowAnimations(R.style.ActivityAnimation);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ViewUtils.inject(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//友盟数据统计
		MobclickAgent.onResume(this);
		//jpush推送统计
		//		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		//		JPushInterface.onPause(this);
	}
	/**
	 * 添加一个广播接收器
	 * 可以快速注册接收器并不必担心注销问题
	 * @param action
	 * @param receiver
	 */
	public void addBroadReceiver(String action,BroadcastReceiver receiver){
          if ( receiverList == null ) {
        	  receiverList = new ArrayList<BroadcastReceiver>();
          }
          IntentFilter intentFilter = new IntentFilter(action);
          registerReceiver(receiver, intentFilter);
		receiverList.add(receiver);
	}

	@Override
	protected void onStop() {
			super.onStop();
			if (receiverList != null ) {
				for ( BroadcastReceiver receiver : receiverList) {
					this.unregisterReceiver(receiver);
				}
			}
		}
	@Override
	protected void onDestroy() {
		RentApplication.getInstance().removeActivity(this);
		super.onDestroy();
	}
	@OnClick(R.id.left_layout)
	public void doLeftLayout(View view) {
		clickLeftLayout(view);
	}
	@OnClick(R.id.right_layout)
	public void doRightLayout(View view) {
		clickRightLayout(view);
	}
	
	//点击title栏左右标题时调用，需要时重写
	public void clickLeftLayout(View view){}
	public void clickRightLayout(View view){}
	
	//--------------------------通用方法设置标题栏－－－－－－
	
	protected void setTitleLeftIcon(boolean show, int resid) {
		ImageView view = (ImageView) findViewById(R.id.title_iv_left);
		if (show) {
			view.setVisibility(View.VISIBLE);
			view.setImageResource(resid);
		} else {
			view.setVisibility(View.GONE);
		}
	}
	protected void setTitleRigthIcon(boolean show, int resid) {
		ImageView view = (ImageView) findViewById(R.id.title_iv_rigth);
		if (show) {
			view.setVisibility(View.VISIBLE);
			view.setImageResource(resid);
		} else {
			view.setVisibility(View.GONE);
		}
	}
	protected void setTitleLeftText(boolean show, String text) {
		TextView view = (TextView) findViewById(R.id.title_tv_left);
		if (show) {
			view.setVisibility(View.VISIBLE);
			view.setText(text);
		} else {
			view.setVisibility(View.GONE);
		}
	}
	protected void setTitleRightText(boolean show, String text) {
		TextView view = (TextView) findViewById(R.id.title_tv_rigth);
		if (show) {
			view.setVisibility(View.VISIBLE);
			view.setText(text);
		} else {
			view.setVisibility(View.GONE);
		}
	}
	protected void setTitleText(boolean show, String text) {
		TextView view = (TextView) findViewById(R.id.title_tv_title);
		if (show) {
			view.setVisibility(View.VISIBLE);
			view.setText(text);
		} else {
			view.setVisibility(View.GONE);
		}
	}
	protected void setTitleTitleBg(int color) {
		RelativeLayout view = (RelativeLayout) findViewById(R.id.title_bg);
		view.setBackgroundColor(color);
	}
}
