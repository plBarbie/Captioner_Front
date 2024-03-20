package com.example.captioner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.captioner.model.PlayBean;

import java.util.List;

public class BookingAdapter extends BaseQuickAdapter<PlayBean, BaseViewHolder> {
    public BookingAdapter( @Nullable List<PlayBean> data) {
        super(R.layout.booked_list_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PlayBean play) {
        baseViewHolder.setText(R.id.play_title,play.getTitle());
        baseViewHolder.setText(R.id.play_start_time,"Start Time: "+play.getStartTime());
        baseViewHolder.setText(R.id.play_end_time,"End   Time: "+play.getEndTime());

    }
}
