package com.qilu.ec.main.user;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joanzapata.iconify.widget.IconTextView;
import com.qilu.core.delegates.QiluDelegate;
import com.qilu.core.ec.R;
import com.qilu.core.net.RestClient;
import com.qilu.core.util.callback.CallbackManager;
import com.qilu.core.util.callback.CallbackType;
import com.qilu.core.util.callback.IGlobalCallback;
import com.qilu.ec.main.sample.example_create.ExampleCreate;
import com.qilu.ec.sign.ISignListener;
import com.qilu.ui.image.GlideTools;

import java.util.Objects;

public class ExampleCreateDelegate extends QiluDelegate implements View.OnClickListener {
    private EditText editText;      //文字输入框
    private ImageView imageView;    //图片区域

    private String img_1_path;//图片路径
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
        return R.layout.example_create;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //添加图片按钮
        IconTextView button_1 = rootView.findViewById(R.id.button_1);
        button_1.setOnClickListener(this);
        //发表按钮
        Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(this);
        editText = rootView.findViewById(R.id.text);
        imageView = rootView.findViewById(R.id.only_img);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_1) {
            //拍照
            ExampleCreateDelegate temp = this;
            CallbackManager.getInstance().addCallback(CallbackType.ON_CROP, (IGlobalCallback<Uri>) args -> {
                // args是照片保存在硬盘上的地址
                if (args != null) {
                    img_1_path = args.getPath();
                    GlideTools.showImagewithGlide(temp, img_1_path, $(R.id.only_img));
                }
            });
            startCameraWithCheck();
        }
        else if (v.getId() == R.id.button) {
            //文本内容
            String text = String.valueOf(editText.getText());
            if (text == null || text.trim().equals("")) {
                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle("错误")
                        .setMessage("描述不可为空")
                        .setPositiveButton("确定", null)
                        .show();
            }
            else if (img_1_path != null && !img_1_path.equals("")) { //图片路径不为空
                //TODO 图片处理
                Log.i("img_path", img_1_path);
                uploadExample(getContext(), text, img_1_path);
            }
            else {
                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle("错误")
                        .setMessage("图片不可为空")
                        .setPositiveButton("确定", null)
                        .show();
            }
            Toast.makeText(getContext(), "假装上传", Toast.LENGTH_SHORT).show();
        }
        else if (v.getId() == R.id.only_img) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle(null);
            builder.setMessage("确定移除？");
            builder.setPositiveButton("是", (dialog, which) -> {
                img_1_path = null;
                //GlideTools.showImagewithGlide(getContext(), null, (ImageView) $(R.id.only_img));
                imageView.setImageDrawable(null);
            });
            builder.setNegativeButton("否", (dialog, which) -> {
            });
            builder.show();
        }
    }

    private void uploadExample(Context context, String text, String img_path) {
        RestClient.builder()
                .url("/star/publish")
                .file("images", img_path)
                .params("content", text)
                .loader(context)
                .success(response -> {
                    //始终会成功
                    Gson gson = new Gson();
                    ExampleCreate exampleCreate = gson.fromJson(response, ExampleCreate.class);
                    int code = exampleCreate.getCode();
                    if (code == 401) {
                        mISignListener.onTokenExpired();
                    } else if (code == 200) {
                        Toast.makeText(context, "成功！", Toast.LENGTH_SHORT).show();
                        getSupportDelegate().pop();
                    }
                })
                .failure(() -> Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show())
                .error((code, msg) -> {
                    if (code == 401) {
                        //Token失效
                        mISignListener.onTokenExpired();
                    } else {
                        Toast.makeText(context, "Error! Msg: " + msg + " Code: " + code, Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .postWithFilesWithToken();
    }
}
