package com.qilu.ec.main.example;

import android.app.Activity;
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

import com.google.gson.Gson;
import com.joanzapata.iconify.widget.IconTextView;
import com.qilu.core.delegates.bottom.BottomItemDelegate;
import com.qilu.core.ec.R;
import com.qilu.core.net.RestClient;
import com.qilu.core.util.storage.UserCollectionHelper;
import com.qilu.ec.main.sample.ExampleItem;
import com.qilu.ec.main.sample.example_list.ExampleResponse;
import com.qilu.ec.main.sample.example_list.ExampleResponse_Data_Star;
import com.qilu.ec.sign.ISignListener;

import java.util.ArrayList;
import java.util.Collections;

public class ExampleDelegate extends BottomItemDelegate implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ISignListener mISignListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    @Override
    public Object setLayout() {
//        return R.layout.delegate_example_item;
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        recyclerView = rootView.findViewById(R.id.list);
        if (recyclerView != null) {
            Context context = rootView.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            //请求数据
            requestDatas();
        }
        IconTextView refresh = rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void requestDatas() {
        RestClient.builder()
                .url("http://106.13.96.60:8888/star/all")
                .loader(getProxyActivity())
                .success(response -> {
                    Gson gson = new Gson();
                    ExampleResponse exampleResponse = gson.fromJson(response, ExampleResponse.class);
                    if (exampleResponse.getCode() == 401) {
                        //Token失效
                        mISignListener.onTokenExpired();
                    } else {
                        ExampleResponse_Data_Star[] stars = exampleResponse.getData().getStars();   //获得示例列表
                        Log.i("初步获得的示例数量", String.valueOf(stars.length));
                        ArrayList<ExampleItem> exampleItems = generateExampleItem(stars);    //获得用于加入adapter的列表
                        Log.i("exampleItems最终的示例数量", String.valueOf(exampleItems.size()));
                        recyclerView.setAdapter(new MyExampleRecyclerViewAdapter(getContext(), /*generateDatas()*/exampleItems));
                    }
                })
                .error((code, msg) -> {
                    if (code == 401) {
                        //Token失效
                        mISignListener.onTokenExpired();
                    }
                })
                .build()
                .getWithToken();
    }

    /**
     * 生成ExampleItem，并根据本地缓存确定每个的isSaved
     *
     * @param stars - 网络请求到的返回数据
     */
    private ArrayList<ExampleItem> generateExampleItem(ExampleResponse_Data_Star[] stars) {
        UserCollectionHelper userCollectionHelper = new UserCollectionHelper(getContext());
        SQLiteDatabase db = userCollectionHelper.getWritableDatabase();
        ArrayList<ExampleItem> exampleItems = new ArrayList<>();
        //查找
        Cursor cursor = db.query(UserCollectionHelper.TABLE_NAME, null, null, null, null, null, null);
        Log.i("本地数据库条数", String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            int i = 0;
            int hasCheck = 0; //已查出的条目数
            Log.i("本地数据库条数(2)", String.valueOf(cursor.getCount()));
            int allNum = cursor.getCount();   //本地总记录数
            for (; i < stars.length && hasCheck < allNum; i++) {
                //对示例逐个比对
                ExampleItem exampleItem = new ExampleItem();
                exampleItem.setId(stars[i].getID());
                exampleItem.setContent(stars[i].getContent());
                exampleItem.setImage(stars[i].getImages());
                //判断是否已收藏
                String itemId = stars[i].getID();
                Log.i("现在要判断的示例图ID", itemId);
                cursor.moveToFirst();   //移动到首位
                boolean has = false;
                for (int j = 0; j < cursor.getCount(); j++) {
                    String id = cursor.getString(cursor.getColumnIndex(UserCollectionHelper.ID)); //取出记录中的id
                    Log.i("现在记录的id", id);
                    if (itemId.equals(id)) {
                        //说明已收藏
                        Log.i("判断", "相同");
                        hasCheck++;
                        has = true;
                        break;
                    } else {
                        Log.i("判断", "不相同");
                        cursor.moveToNext();
                    }
                }
                if (has) {
                    //已收藏
                    exampleItem.setSaved(true);     //设为true
                } else {
                    exampleItem.setSaved(false);    //设为false
                }
                exampleItems.add(exampleItem);
            }
            if (hasCheck == allNum) {

                //说明已经查完了但还没有到末尾
                for (; i < stars.length; i++) {
                    exampleItems.add(new ExampleItem(stars[i].getID(), stars[i].getContent(), stars[i].getImages(), false));//剩余的设为false
                }
            }
        } else {
            //正常插入即可
            for (ExampleResponse_Data_Star star : stars) {
                exampleItems.add(new ExampleItem(star.getID(), star.getContent(), star.getImages(), false));
            }
            Log.i("exampleItems的最终size", String.valueOf(exampleItems.size()));
        }
        cursor.close();
        db.close();
        Collections.reverse(exampleItems);
        return exampleItems;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.refresh) {
            //刷新界面
            requestDatas();
        }
    }
}
