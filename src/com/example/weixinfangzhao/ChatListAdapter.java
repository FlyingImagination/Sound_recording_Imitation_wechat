package com.example.weixinfangzhao;

import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChatListAdapter extends ArrayAdapter<ChatRecorder> {

	private Context mContext;
	private int itemMinWidth;
	private int itemMaxWidth;
	private LayoutInflater minInflater;

	public ChatListAdapter(Context context, List<ChatRecorder> datas) {
		super(context, -1, datas);
		mContext = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		itemMaxWidth = (int) (metrics.widthPixels * 0.7f);
		itemMinWidth = (int) (metrics.widthPixels * 0.15f);
		minInflater = LayoutInflater.from(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = minInflater.inflate(R.layout.item_record, parent,
					false);
			holder.seconds = (TextView) convertView
					.findViewById(R.id.recordTime);
			holder.length = convertView.findViewById(R.id.recordLength);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.seconds.setText((int)getItem(position).time + "'");
		ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
		lp.width = (int) (itemMinWidth + (itemMaxWidth / 60f * getItem(position).time));
		return convertView;
	}

	class ViewHolder {
		TextView seconds;
		View length;
	}

}
