package com.qilu.ec.main.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.qilu.core.delegates.QiluDelegate;
import com.qilu.core.ec.R;
import com.qilu.core.util.storage.ImageHistoryHelper;
import com.qilu.ec.main.sample.DecorateHistory;

import java.util.ArrayList;
import java.util.Collections;

@SuppressLint("ValidFragment")
public class HistoryDelegate extends QiluDelegate {
    RecyclerView recyclerView;

    @Override
    public Object setLayout() {
        return R.layout.delegate_decorate_history;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        recyclerView = rootView.findViewById(R.id.list);
        if (recyclerView != null) {
            Context context = rootView.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            requestDatas();
        }
    }

    // TODO 小Bug：加载条不显示
    private void requestDatas() {
        ImageHistoryHelper imageHistoryHelper = new ImageHistoryHelper(getContext());
        SQLiteDatabase db = imageHistoryHelper.getWritableDatabase();
        ArrayList<DecorateHistory> decorateHistories = new ArrayList<>();
        //查找
        @SuppressLint("Recycle")
        Cursor cursor = db.query(ImageHistoryHelper.TABLE_NAME, null, null, null, null, null, null);
        Log.i("本地数据库条数", String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            //生成加入adapter中的数据
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Log.i("加入", String.valueOf(i));
                decorateHistories.add(
                        new DecorateHistory(
                                cursor.getString(cursor.getColumnIndex(ImageHistoryHelper.TIME)),
                                cursor.getString(cursor.getColumnIndex(ImageHistoryHelper.IMAGE))
                        ));
                cursor.moveToNext();
            }
            Collections.reverse(decorateHistories);
            Log.i("size大小", String.valueOf(decorateHistories.size()));
            recyclerView.setAdapter(new HistoryRecyclerViewAdapter(getContext(), decorateHistories));
        }
        db.close();
    }
}
