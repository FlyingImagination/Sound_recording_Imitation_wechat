package com.example.weixinfangzhao;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.weixinfangzhao.ChatRecorderButton.AudioFinishRecorderListener;

public class MainActivity extends Activity {

	private ChatRecorderButton audio_button;

	private ListView mListView;
	private ArrayAdapter<ChatRecorder> mAdapter;
	private List<ChatRecorder> mDates = new ArrayList<ChatRecorder>();
	private View animView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		audio_button = (ChatRecorderButton) findViewById(R.id.audio_button);
		mListView = (ListView) findViewById(R.id.mListView);
		mAdapter = new ChatListAdapter(MainActivity.this, mDates);
		mListView.setAdapter(mAdapter);
		audio_button
				.setAudioFinishRecorderListener(new AudioFinishRecorderListener() {

					@Override
					public void onFinish(float seconds, String filepath) {
						ChatRecorder recorder = new ChatRecorder(seconds, filepath);
						mDates.add(recorder);
						mAdapter.notifyDataSetChanged();
						mListView.setSelection(mDates.size() - 1);
					}
				});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (animView != null) {
					animView.setBackgroundResource(R.drawable.adj);
					animView = null;
				}
				// 播放动画
				animView = view.findViewById(R.id.id_record_anim);
				animView.setBackgroundResource(R.drawable.play_anim_finish);
				AnimationDrawable animationDrawable = (AnimationDrawable) animView
						.getBackground();
				animationDrawable.start();
				// 播放录音
				ChatMediaManager.playSound(mDates.get(position).getFilePath(),
						new MediaPlayer.OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer mp) {
								animView.setBackgroundResource(R.drawable.adj);
							}
						});
			}
		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ChatMediaManager.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ChatMediaManager.resume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ChatMediaManager.release();
	}

}
