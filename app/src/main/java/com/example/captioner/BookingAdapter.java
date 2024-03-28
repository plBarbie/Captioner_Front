package com.example.captioner;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.captioner.model.PlayBean;
import com.example.captioner.model.PlayDTO;
import com.example.captioner.network.BookService;
import com.example.captioner.network.RetrofitClient;
import com.example.captioner.network.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingAdapter extends BaseQuickAdapter<PlayBean, BaseViewHolder> {
    public BookingAdapter(@Nullable List<PlayBean> data) {
        super(R.layout.booking_list_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PlayBean play) {
        baseViewHolder.setText(R.id.play_title, play.getTitle());
        baseViewHolder.setText(R.id.play_start_time, "Start Time: " + play.getStartTime());
        baseViewHolder.setText(R.id.play_end_time, "End Time: " + play.getEndTime());

        // 为“Book”按钮设置点击监听器
        baseViewHolder.getView(R.id.book_button).setOnClickListener(v -> {
            // 创建 PlayDTO 仅包含 title 用于预订操作
            PlayDTO playDTO = new PlayDTO();
            playDTO.setTitle(play.getTitle());

            // 使用Retrofit发起网络请求
            BookService service = RetrofitClient.getClient("http://10.29.144.153:8014/").create(BookService.class);
            Call<UserResponse> call = service.bookPlay(playDTO);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful()) {
                        // 预订成功
                        Toast.makeText(getContext(), "Booked successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        // 处理错误情况
                        Toast.makeText(getContext(), "Booking failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    // 网络错误处理
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
