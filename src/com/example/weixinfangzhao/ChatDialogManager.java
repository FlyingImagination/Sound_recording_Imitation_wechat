package com.example.weixinfangzhao;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 录音提示框管理类
 * 
 * @author JYY
 *
 */
@SuppressLint("InflateParams")
public class ChatDialogManager {

	private Dialog mDialog;

	private ImageView mIcon;
	private ImageView mVoice;
	private TextView mlable;

	private Context mContext;

	public ChatDialogManager(Context context) {
		mContext = context;
	}

	public void showRecording() {
		// TODO Auto-generated method stub
		mDialog = new Dialog(mContext, R.style.Theme_audio_dialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_record, null);
		mDialog.setContentView(view);
		mIcon = (ImageView) view.findViewById(R.id.record_dialog_icon);
		mVoice = (ImageView) view.findViewById(R.id.record_dialog_voice);
		mlable = (TextView) view.findViewById(R.id.record_dialog_label);
		mDialog.show();
	}

	/**
	 * 
	 *@Title recording
	 *@Description 开始录音
	 *void
	 *@author JYY
	 *@date 2015年10月22日
	 */
	public void recording() {
		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mlable.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.drawable.recorder);
			mlable.setText("手指上滑，取消发送");
		}

	}
	/**
	 * 
	 *@Title wantToCancel
	 *@Description 取消录音
	 *void
	 *@author JYY
	 *@date 2015年10月22日
	 */
	public void wantToCancel() {
		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mlable.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);

			mIcon.setImageResource(R.drawable.cancel);
			mlable.setText("松开手指，取消发送");
		}
	}

	/**
	 * 
	 *@Title tooShort
	 *@Description 录音时间过短提示
	 *void
	 *@author JYY
	 *@date 2015年10月22日
	 */
	public void tooShort() {
		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mlable.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);

			mIcon.setImageResource(R.drawable.voice_to_short);
			mlable.setText("录音时间过短");
		}

	}

	public void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	/**
	 * 
	 *@Title updateVoicelevel
	 *@Description 展示音量大小更新
	 *@param level
	 *void
	 *@author JYY
	 *@date 2015年10月22日
	 */
	public void updateVoicelevel(int level) {
		if (mDialog != null && mDialog.isShowing()) {
			int resId = mContext.getResources().getIdentifier("v" + level,
					"drawable", mContext.getPackageName());
			mVoice.setImageResource(resId);
		}
	}
}
