package com.lezyo.travel.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lezyo.fsm.R;
import com.lezyo.fsm.RentApplication;
import com.lezyo.fsm.bean.UpdateEntity;

/**
 * 自动更新对话框,作了主要逻辑处理
 * @author di.chao
 */
public class CustomUpdateDialog extends Dialog {
	private Context context;
    private TextView title;
    private TextView updateContext;
    private Button okBtn;
    private Button cancle;
    private UpdateDialogListener listener;
	public CustomUpdateDialog(Context context ,final UpdateEntity bean) {
		super(context, R.style.CustomProgressDialog);
		this.context = context;
		setContentView(R.layout.update_dialog_layout);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		this.setCancelable(true);
		title = (TextView) this.findViewById(R.id.update_title);
		updateContext = (TextView) this.findViewById(R.id.update_context);
        cancle = (Button) this.findViewById(R.id.update_cancle);
		okBtn = (Button) this.findViewById(R.id.update_ok);
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomUpdateDialog.this.dismiss();
			}
		});
		switch (bean.getIs_update()) {
		//有更新
		case 1:
			title.setText("发现新版本，更新内容为：");
			okBtn.setText("立即更新");
			cancle.setText("取消");
			break;
	   //强制更新
		case 2:
			title.setText("发现新版本，需要更新才能继续使用：");
			okBtn.setText("立即更新");
			cancle.setText("退出");
			this.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					RentApplication.getInstance().finishAll();
				}
			});
			break;
		}
		updateContext.setText(bean.getUpdate_desc());
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Uri updateUrl = Uri.parse(bean.getUpdate_uri());
				Intent intent = new Intent(Intent.ACTION_VIEW,updateUrl);
				CustomUpdateDialog.this.context.startActivity(intent);
			}
		});
	}
	public void setBtnListener (UpdateDialogListener listener) {
		this.listener = listener;
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (CustomUpdateDialog.this.listener != null) {
					CustomUpdateDialog.this.listener.doUpdateButton(okBtn, CustomUpdateDialog.this);
			
				}
			}
		});
	}
	public void setUpdateText (String text) {
		okBtn.setText(text);
	}
	public void setChannelText (String text) {
		cancle.setText(text);
	}
	/**
	 * 设置消息提示
	 */
	public void setMessage(String strMessage) {
		title.setText(strMessage);
	}
   public interface UpdateDialogListener {
	   public void doUpdateButton(Button btn , CustomUpdateDialog dialog);
   }

}
