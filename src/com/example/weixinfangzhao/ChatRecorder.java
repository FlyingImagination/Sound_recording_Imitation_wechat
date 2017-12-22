package com.example.weixinfangzhao;

import java.io.Serializable;

public class ChatRecorder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 时间长度
	 */
	float time; 
	/**
	 * 存放路径
	 */
	String filePath;

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public ChatRecorder(float time, String filePath) {
		super();
		this.time = time;
		this.filePath = filePath;
	}

}
