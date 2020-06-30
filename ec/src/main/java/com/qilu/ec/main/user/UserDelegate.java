package com.qilu.ec.main.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.qilu.core.delegates.bottom.BottomItemDelegate;
import com.qilu.core.ec.R;
import com.qilu.core.net.RestClient;
import com.qilu.ec.main.option.OptionDelegate;
import com.google.gson.Gson;
import com.qilu.ec.main.sample.user_profile.UserProfile;
import com.qilu.ec.main.sample.user_profile.UserProfile_Data_Data;
import com.qilu.ec.main.util.Image;
import com.qilu.ec.sign.ISignListener;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("ValidFragment")
public class UserDelegate extends BottomItemDelegate implements View.OnClickListener {
    private UserProfile_Data_Data data;

    private CircleImageView user_img;
    private TextView text_name;

    private ISignListener mISignListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    @SuppressLint("ValidFragment")
    public UserDelegate() {
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_me;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        IconTextView optionImage = rootView.findViewById(R.id.option);
        optionImage.setOnClickListener(this);
        user_img = rootView.findViewById(R.id.user_img);
        text_name = rootView.findViewById(R.id.text_name);
        RelativeLayout star = rootView.findViewById(R.id.star);
        RelativeLayout history = rootView.findViewById(R.id.history);
        RelativeLayout upload = rootView.findViewById(R.id.upload);
        star.setOnClickListener(this);
        history.setOnClickListener(this);
        upload.setOnClickListener(this);
        RestClient.builder()
                .url("/account")
                .loader(getContext())
                .success(response -> {
                    Gson gson = new Gson();
                    UserProfile userProfile = gson.fromJson(response, UserProfile.class);
                    if (userProfile.getCode() == 401) {
                        mISignListener.onTokenExpired();
                    } else {
                        data = userProfile.getData().getData();
                        if (data != null) {
                            Image.showResultImage(data.getAvatar(), user_img);
                            text_name.setText(data.getUserName());
                        } else {
                            Log.e("data", "用户信息为空！");
                            Toast.makeText(getContext(), "用户信息获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .failure(() -> Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show())
                .error((code, msg) -> {
                    if (code == 401) {
                        //Token失效
                        mISignListener.onTokenExpired();
                    }
                    Toast.makeText(getContext(), "请求失败，" + code + "，" + msg, Toast.LENGTH_LONG).show();
                })
                .build()
                .getNoParamsWithToken();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.option) {
            getParentDelegate().getSupportDelegate().start(new OptionDelegate(data, this));
        } else if (v.getId() == R.id.star) {
            //跳转到示例收藏
            getParentDelegate().getSupportDelegate().start(new StarDelegate());
        } else if (v.getId() == R.id.history) {
            //跳转到美妆历史
            getParentDelegate().getSupportDelegate().start(new HistoryDelegate());
        } else if (v.getId() == R.id.upload) {
            //跳转到上传示例
            getParentDelegate().getSupportDelegate().start(new ExampleCreateDelegate());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RestClient.builder()
                .url("/account")
                .loader(getContext())
                .success(response -> {
                    Gson gson = new Gson();
                    UserProfile userProfile = gson.fromJson(response, UserProfile.class);
                    if (userProfile.getCode() == 401) {
                        mISignListener.onTokenExpired();
                    } else {
                        data = userProfile.getData().getData();
                        if (data != null) {
                            Image.showResultImage(data.getAvatar(), user_img);
                            text_name.setText(data.getUserName());
                        } else {
                            Log.e("data", "用户信息为空！");
                            Toast.makeText(getContext(), "用户信息获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .failure(() -> Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show())
                .error((code, msg) -> {
                    if (code == 401) {
                        //Token失效
                        mISignListener.onTokenExpired();
                    } else {
                        Toast.makeText(getContext(), "请求失败，" + code + "，" + msg, Toast.LENGTH_LONG).show();
                    }
                })
                .build()
                .getNoParamsWithToken();
    }
}
