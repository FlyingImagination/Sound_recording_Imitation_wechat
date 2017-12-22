package com.example.weixinfangzhao;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;

public class ChatAudioManager {

	private MediaRecorder mediaRecorder;

	private String mDir;
	private String mCurrentFilepath;

	public String getmCurrentFilepath() {
		return mCurrentFilepath;
	}

	private boolean isPrepared;

	private static ChatAudioManager mInstance;

	private ChatAudioManager(String dir) {
		mDir = dir;
	}

	/**
	 * 回调准备完毕
	 * 
	 * @author JYY
	 *
	 */
	public interface AudioStateListerner {
		void wellPrepared();
	}

	public AudioStateListerner mListerner;

	public void setOnAudioStateListener(AudioStateListerner listerner) {
		mListerner = listerner;
	}

	public static ChatAudioManager getInstance(String dir) {
		if (mInstance == null) {
			synchronized (ChatAudioManager.class) {
				if (mInstance == null) {
					mInstance = new ChatAudioManager(dir);
				}
			}
		}
		return mInstance;
	}

	/**
	 * @Title prepareAudio
	 * @Description TODO 准备录音 void
	 * @author JYY
	 * @date 2015年10月22日
	 */
	@SuppressWarnings("deprecation")
	public void prepareAudio() {
		try {
			isPrepared = false;
			File dir = new File(mDir);
			if (!dir.exists())
				dir.mkdirs();
			String fileName = generateFilename();
			File file = new File(mDir, fileName);
			mCurrentFilepath = file.getAbsolutePath();
			mediaRecorder = new MediaRecorder();
			// 设置输出文件
			mediaRecorder.setOutputFile(file.getAbsolutePath());
			// 设置音频元为麦克风
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置音频格式
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			// 设置音频编码
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			mediaRecorder.prepare();
			mediaRecorder.start();
			isPrepared = true;
			if (mListerner != null) {
				mListerner.wellPrepared();
			}

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @Title generateFilename
	 * @Description TODO 生成文件名
	 * @return String
	 * @author JYY
	 * @date 2015年10月22日
	 */
	private String generateFilename() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString() + ".amr";
	}

	/**
	 * 
	 * @Title getVoiceLevel
	 * @Description TODO 获取声音等级
	 * @param maxLevel
	 * @return int
	 * @author JYY
	 * @date 2015年10月22日
	 */
	public int getVoiceLevel(int maxLevel) {
		if (isPrepared) {
			try {
				// mediaRecorder.getMaxAmplitude() 1-32767
				if (mediaRecorder != null) {
					return maxLevel * mediaRecorder.getMaxAmplitude() / 32768
							+ 1;
				}
			} catch (IllegalStateException e) {
			}
		}
		return 1;
	}

	/**
	 * @Title release
	 * @Description TODO 释放 void
	 * @author JYY
	 * @date 2015年10月22日
	 */
	public void release() {
		mediaRecorder.stop();
		mediaRecorder.release();
		mediaRecorder = null;
	}

	/**
	 * @Title cancel
	 * @Description TODO 取消 void
	 * @author JYY
	 * @date 2015年10月22日
	 */
	public void cancel() {
		release();
		if (mCurrentFilepath != null) {
			File file = new File(mCurrentFilepath);
			file.delete();
			mCurrentFilepath = null;
		}
	}

}
