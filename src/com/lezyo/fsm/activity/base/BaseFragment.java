package com.lezyo.fsm.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lezyo.fsm.R;
import com.lezyo.travel.customview.CustomDialog;
import com.lezyo.travel.customview.CustomProgressDialog;
import com.lidroid.xutils.ViewUtils;
/**
 * 每一个fragment都继承该类
 *
 */
public abstract class BaseFragment extends Fragment {
	protected Activity mContext;
	protected CustomDialog dialog;
	private View mainView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this.getActivity();
	
		dialog = new CustomDialog( mContext );
		LayoutInflater inflater = LayoutInflater.from( mContext );
		mainView  = createView( inflater);
		ViewUtils.inject(this , mainView);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup parent = (ViewGroup) mainView.getParent();  
		if (parent != null) {  
			parent.removeView(mainView);  
		}   
		return  mainView ;
	}
	public View findViewById (int resId) {
		if (mainView == null) {
			return null;
		}
		return mainView.findViewById(resId);
	}
	
	/**
	 * 返回fragemtn要创建的View
	 * 不必重写 onCreateView 只需从写改方法
	 * */
	protected abstract View createView(LayoutInflater inflater);
	
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
