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
import com.qilu.core.util.storage.UserCollectionHelper;
import com.qilu.ec.main.sample.ExampleItem;

import java.util.ArrayList;

public class StarDelegate extends QiluDelegate {
    RecyclerView recyclerView;

    @Override
    public Object setLayout() {
        return R.layout.delegate_star;
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

    private void requestDatas() {
        UserCollectionHelper userCollectionHelper = new UserCollectionHelper(getContext());
        SQLiteDatabase db = userCollectionHelper.getWritableDatabase();
        ArrayList<ExampleItem> exampleItems = new ArrayList<>();
        //查找
        @SuppressLint("Recycle")
        Cursor cursor = db.query(UserCollectionHelper.TABLE_NAME, null, null, null, null, null, null);
        Log.i("本地数据库条数", String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            //生成加入adapter中的数据
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Log.i("加入", String.valueOf(i));
                exampleItems.add(
                        new ExampleItem(
                                cursor.getString(cursor.getColumnIndex(UserCollectionHelper.ID)),
                                cursor.getString(cursor.getColumnIndex(UserCollectionHelper.CONTENT)),
                                cursor.getString(cursor.getColumnIndex(UserCollectionHelper.IMAGE)),
                                true    //收藏的一定都是true
                        )); //取出记录中的id
                cursor.moveToNext();
            }
            Log.i("size大小", String.valueOf(exampleItems.size()));
            recyclerView.setAdapter(new StarRecyclerViewAdapter(this, /*generateDatas()*/exampleItems));
        }
    }
}
