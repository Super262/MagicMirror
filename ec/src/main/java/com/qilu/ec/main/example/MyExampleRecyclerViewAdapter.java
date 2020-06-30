package com.qilu.ec.main.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.qilu.core.ec.R;
import com.qilu.core.util.storage.UserCollectionHelper;
import com.qilu.ec.main.sample.ExampleItem;
import com.qilu.ec.main.util.Image;

import java.util.List;

public class MyExampleRecyclerViewAdapter extends RecyclerView.Adapter<MyExampleRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<ExampleItem> mValues;

    public MyExampleRecyclerViewAdapter(Context context, List<ExampleItem> items) {
        this.context = context;
        mValues = items;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delegate_example_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ExampleItem exampleItem = mValues.get(position);
        holder.textView.setText(exampleItem.getContent());
//        holder.imageView.setImageDrawable(exampleItem.getImage());
        Image.showResultImage(exampleItem.getImage(), holder.imageView);
        holder.isSaved = exampleItem.getSaved();
        if (holder.isSaved) {
            holder.buttonView.setText(R.string.starPlused);
        } else {
            holder.buttonView.setText(R.string.starToPlus);
        }
        holder.buttonView.setOnClickListener(v -> {
            //只有buttonView监听click
            UserCollectionHelper userCollectionHelper = new UserCollectionHelper(context);
            SQLiteDatabase db = userCollectionHelper.getWritableDatabase();
            if (exampleItem.getSaved()) {
                //已收藏
                Log.i("是否收藏", "是->否");
                int result = db.delete(UserCollectionHelper.TABLE_NAME, UserCollectionHelper.ID + "=?", new String[]{exampleItem.getId()});
                if (result > 0) {
                    // 成功
                    ((IconTextView) v).setText(R.string.starToPlus);
                    exampleItem.setSaved(false);
                }
            } else {
                //未收藏
                Log.i("是否收藏", "否->是");
                String id = exampleItem.getId();
                Log.i("收藏的ID", id);
                String content = exampleItem.getContent();
                //假设接口的图片是Base64字符串
                String img_base64;
                //如果get返回的是Drawable
                //img_base64 = Image.BitmapToStrByBase64(Image.drawableToBitmap(exampleItem.getImage()));
                //如果get返回的是Base64的String
                img_base64 = exampleItem.getImage();
                // 示例

                ContentValues values = new ContentValues();
                values.put(UserCollectionHelper.ID, id);
                values.put(UserCollectionHelper.CONTENT, content);
                values.put(UserCollectionHelper.IMAGE, img_base64);

                long result = db.insert(UserCollectionHelper.TABLE_NAME, null, values);
                if (result > 0) {
                    // 成功
                    exampleItem.setSaved(true);
                    ((IconTextView) v).setText(R.string.starPlused);
                    exampleItem.setSaved(true);
                }
            }
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
        View view;
        IconTextView buttonView;
        TextView textView;
        ImageView imageView;
        Boolean isSaved;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            buttonView = view.findViewById(R.id.button);
            textView = view.findViewById(R.id.text_content);
            imageView = view.findViewById(R.id.image);
        }
    }
}