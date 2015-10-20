package com.lezyo.fsm.pullrefresh.view;

import com.lezyo.fsm.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class PullToRefreshLinearLayout extends PullToRefreshBase<LinearLayout>{

	public PullToRefreshLinearLayout(Context context) {
		super(context);
	}

	public PullToRefreshLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshLinearLayout(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshLinearLayout(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}


	@Override
	protected LinearLayout createRefreshableView(Context context,AttributeSet attrs) {
		LinearLayout linearLayout;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			linearLayout = new InternalScrollViewSDK9(context, attrs);
		} else {
			linearLayout = new LinearLayout(context, attrs);
		}

		linearLayout.setId(R.id.linearLayout);
		return linearLayout;
	}
	

	@TargetApi(9)
	final class InternalScrollViewSDK9 extends LinearLayout {

		public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshLinearLayout.this, deltaX, scrollX, deltaY, scrollY,
					getScrollRange(), isTouchEvent);

			return returnValue;
		}

		private int getScrollRange() {
			int scrollRange = 0;
			if (getChildCount() > 0) {
				View child = getChildAt(0);
				scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
			}
			return scrollRange;
		}
	}

	@Override
	protected boolean isReadyForPullEnd() {
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}
		return false;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	public com.lezyo.fsm.pullrefresh.view.PullToRefreshBase.Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}


}
