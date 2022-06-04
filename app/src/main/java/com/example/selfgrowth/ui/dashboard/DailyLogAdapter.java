package com.example.selfgrowth.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.model.DailyLogModel;
import com.example.selfgrowth.utils.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class DailyLogAdapter extends RecyclerView.Adapter<DailyLogAdapter.TimeLineViewHolder> {

    private final List<DailyLogModel> data;

    public DailyLogAdapter(final List<DailyLogModel> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater flater = LayoutInflater.from(parent.getContext());
        return new TimeLineViewHolder(flater.inflate(R.layout.daily_log_timeline, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        DailyLogModel model = data.get(position);
        if (model.getType().equals(DailyLogModel.DailyLogType.ACTIVITY_BEGIN)) {
            setMarker(holder, R.drawable.activity_begin, R.color.material_grey_500);
        } else if (model.getType().equals(DailyLogModel.DailyLogType.ACTIVITY_END)) {
            setMarker(holder, R.drawable.activity_end, R.color.material_grey_500);
        } else {
            setMarker(holder, R.drawable.task_complete, R.color.material_grey_500);
        }
        holder.message.setText(model.getMessage());
        holder.date.setText(model.getDate());
    }

    private void setMarker(TimeLineViewHolder holder, int drawableResId, int colorFilter) {
        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(holder.itemView.getContext(), drawableResId, ContextCompat.getColor(holder.itemView.getContext(), colorFilter)));
    }

    public static class TimeLineViewHolder extends RecyclerView.ViewHolder {

        public TimelineView mTimelineView;
        public AppCompatTextView date;
        public AppCompatTextView message;
        public View itemView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
            date = itemView.findViewById(R.id.text_timeline_date);
            message = itemView.findViewById(R.id.text_timeline_title);
            this.itemView = itemView;
        }
    }
}
