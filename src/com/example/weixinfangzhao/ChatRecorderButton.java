package com.example.weixinfangzhao;

import com.example.weixinfangzhao.ChatAudioManager.AudioStateListerner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * 自定义录音按钮控件
 * @author JYY
 *
 */
@SuppressLint({ "ClickableViewAccessibility", "HandlerLeak" })
public class ChatRecorderButton extends Button implements AudioStateListerner {

	private static final int DISTANCE = 50;
	private static final int STATE_NORMAL = 1;
	private static final int STATE_RECORDING = 2;
	private static final int STATE_WANT_TO_CANCEL = 3;

	private int mCurState = STATE_NORMAL;
	// 已经开始录音
	private boolean isRecording = false;

	private ChatDialogManager mDialogManager;

	private ChatAudioManager mAudioManager;

	private float mTime = 0;
	// 是否触发longClick
	private boolean mReady;

	public ChatRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mDialogManager = new ChatDialogManager(getContext());

		String dir = Environment.getExternalStorageDirectory()
				+ "/superion_record";
		mAudioManager = ChatAudioManager.getInstance(dir);
		mAudioManager.setOnAudioStateListener(this);
		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				mReady = true;
				mAudioManager.prepareAudio();
				return false;
			}
		});
	}

	// 录音完成后的回调
	public interface AudioFinishRecorderListener {
		public void onFinish(float seconds, String filepath);
	}

	private AudioFinishRecorderListener mListener;

	public void setAudioFinishRecorderListener(
			AudioFinishRecorderListener listener) {
		mListener = listener;
	}

	@Override
	public void wellPrepared() {
		mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
	}

	private Runnable mgetVoiceLevelRunnable = new Runnable() {

		@Override
		public void run() {
			while (isRecording) {
				try {
					Thread.sleep(100);
					mTime += 0.1f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	private static final int MSG_AUDIO_PREPARED = 0x110;
	private static final int MSG_VOICE_CHANGED = 0x111;
	private static final int MSG_DIALOG_DISMISS = 0x112;

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_AUDIO_PREPARED:
				mDialogManager.showRecording();
				isRecording = true;
				new Thread(mgetVoiceLevelRunnable).start();
				break;
			case MSG_VOICE_CHANGED:
				mDialogManager.updateVoicelevel(mAudioManager.getVoiceLevel(7));
				break;
			case MSG_DIALOG_DISMISS:
				mDialogManager.dismissDialog();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			changeState(STATE_RECORDING);
			break;

		case MotionEvent.ACTION_MOVE:
			if (isRecording) {
				// 根据x，y的坐标，判断是否想要取消
				if (wantToCancel(x, y)) {
					changeState(STATE_WANT_TO_CANCEL);
				} else {
					changeState(STATE_RECORDING);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (!mReady) {
				reset();
				return super.onTouchEvent(event);
			}
			if (!isRecording || mTime < 0.6f) {
				mDialogManager.tooShort();
				mAudioManager.cancel();
				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);
			} else if (mCurState == STATE_RECORDING) {
				mDialogManager.dismissDialog();
				// release
				mAudioManager.release();
				// callbackActivity
				if (mListener != null) {
					mListener.onFinish(mTime,
							mAudioManager.getmCurrentFilepath());
				}
			} else if (mCurState == STATE_WANT_TO_CANCEL) {
				// cancel
				mDialogManager.dismissDialog();
				mAudioManager.cancel();
			}
			reset();

			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	// 恢复状态及标志位
	private void reset() {
		// TODO Auto-generated method stub
		isRecording = false;
		mReady = false;
		mTime = 0;
		changeState(STATE_NORMAL);
	}

	private boolean wantToCancel(int x, int y) {

		if (x < 0 || x > getWidth()) {
			return true;
		}
		if (y < -DISTANCE || y > getHeight() + DISTANCE) {
			return true;
		}
		return false;
	}

	private void changeState(int stateRecording) {
		if (mCurState != stateRecording) {
			mCurState = stateRecording;
			switch (stateRecording) {
			case STATE_NORMAL:
				setBackgroundResource(R.drawable.record_button_normal);
				setText(R.string.record_button_normal);
				break;
			case STATE_RECORDING:
				setBackgroundResource(R.drawable.record_button_recording);
				setText(R.string.record_button_recording);
				if (isRecording) {
					mDialogManager.recording();
				}
				break;
			case STATE_WANT_TO_CANCEL:
				setBackgroundResource(R.drawable.record_button_recording);
				setText(R.string.record_button_cancel);
				mDialogManager.wantToCancel();
				break;
			default:
				break;
			}
		}
	}

}
