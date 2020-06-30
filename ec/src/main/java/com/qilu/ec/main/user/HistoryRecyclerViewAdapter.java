package com.qilu.ec.main.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.qilu.core.ec.R;
import com.qilu.ec.main.sample.DecorateHistory;
import com.qilu.ec.main.util.Image;


import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<DecorateHistory> mValues;

    public HistoryRecyclerViewAdapter(Context context, List<DecorateHistory> mValues) {
        this.context = context;
        this.mValues = mValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delegate_decorate_history_item, parent, false);
        return new HistoryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DecorateHistory decorateHistory = mValues.get(position);
        holder.timeView.setText(decorateHistory.getTime());
        Image.showResultImage(decorateHistory.getImg_base64(), holder.imageView);
        holder.button.setOnClickListener(v -> {
            Image.saveImageToGallery(context, Image.base64ToBitmap(decorateHistory.getImg_base64()));
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        if (mValues != null)
            return mValues.size();
        else
            return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeView;
        ImageView imageView;
        IconTextView button;

        public ViewHolder(View view) {
            super(view);
            timeView = view.findViewById(R.id.time);
            imageView = view.findViewById(R.id.image);
            button = view.findViewById(R.id.button);
        }
    }
}
