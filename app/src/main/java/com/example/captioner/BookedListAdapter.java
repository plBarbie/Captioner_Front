package com.example.captioner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.captioner.model.Play;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

public class BookedListAdapter extends ArrayAdapter<Play> {

    public BookedListAdapter(Context context, List<Play> plays) {
        super(context, 0, plays);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取当前项的 Play 实例
        Play play = getItem(position);

        // 检查现有的视图是否被重用，否则填充视图
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.booked_list_item, parent, false);
        }

        // 查找布局中的 TextView
        TextView titleTextView = (TextView) convertView.findViewById(R.id.play_title);
        TextView startTimeTextView = (TextView) convertView.findViewById(R.id.play_start_time);
        TextView endTimeTextView = (TextView) convertView.findViewById(R.id.play_end_time);

        // 填充数据
        titleTextView.setText(play.getTitle());
        // 格式化显示的日期时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        startTimeTextView.setText("开始时间: " + play.getStartTime().format(formatter));
        endTimeTextView.setText("结束时间: " + play.getEndTime().format(formatter));

        // 返回完成的视图以显示在 ListView 中
        return convertView;
    }
}
