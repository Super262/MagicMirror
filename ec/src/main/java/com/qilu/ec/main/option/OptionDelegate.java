package com.qilu.ec.main.option;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qilu.core.delegates.QiluDelegate;
import com.qilu.core.ec.R;
import com.qilu.core.net.RestClient;
import com.qilu.core.util.callback.CallbackManager;
import com.qilu.core.util.callback.CallbackType;
import com.qilu.core.util.callback.IGlobalCallback;
import com.qilu.core.util.storage.QiluPreference;
import com.qilu.ec.main.sample.user_info.UserInfo;
import com.qilu.ec.main.sample.user_profile.UserProfile_Data_Data;
import com.qilu.ec.main.user.UserDelegate;
import com.qilu.ec.sign.ISignListener;

import java.util.Objects;

@SuppressLint("ValidFragment")
public class OptionDelegate extends QiluDelegate implements View.OnClickListener {
    private UserProfile_Data_Data data; //用户当前信息

    private TextView name;
    private TextView passWord;
    private TextView user_img;
    private TextView user_exit;

    private UserDelegate userDelegate;
    private ISignListener mISignListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    @SuppressLint("ValidFragment")
    public OptionDelegate(UserProfile_Data_Data data, UserDelegate userDelegate) {
        this.data = data;
        this.userDelegate = userDelegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.option_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        name = rootView.findViewById(R.id.name);
        passWord = rootView.findViewById(R.id.passWord);
        user_img = rootView.findViewById(R.id.user_img);
        user_exit = rootView.findViewById(R.id.user_exit);
        listenerRegister();
    }

    private void listenerRegister() {
        name.setOnClickListener(this);
        passWord.setOnClickListener(this);
        user_img.setOnClickListener(this);
        user_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.name) {
            showNameDialog();
        } else if (v.getId() == R.id.passWord) {
            showPasswordDialog();
        } else if (v.getId() == R.id.user_img) {
            changeImg();
        } else if (v.getId() == R.id.user_exit) {
            exit();
        }
    }

    private void exit() {
        //退出
        AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        dialog.setTitle("确认");
        dialog.setMessage("确认退出当前账号？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确认", (dialog1, which) -> mISignListener.onTokenExpired());
        dialog.setNegativeButton("取消", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }

    private void changeImg() {
        CallbackManager.getInstance().addCallback(CallbackType.ON_CROP, (IGlobalCallback<Uri>) args -> {
            // args是照片保存在硬盘上的地址
            if (args != null) {
                String img_1_path = args.getPath();
                uploadImg(img_1_path);
            }
        });
        startCameraWithCheck();
    }

    private void uploadImg(String img_path) {
        RestClient.builder()
                .url("http://106.13.96.60:8888/user/"+QiluPreference.getCustomAppProfile("userID")+"/update_avatar")
                .loader(getContext())
                .file("avatar", img_path)
                .success(response -> {
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                    int code = userInfo.getCode();
                    if (code == 401) {
                        mISignListener.onTokenExpired();
                    } else if (code == 200) {              //TODO 感觉code一般应该一定是200
                        Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                        userDelegate.onResume();
                    }
                })
                .error((code, msg) -> {
                    if (code == 401) {
                        //Token失效
                        mISignListener.onTokenExpired();
                    }
                })
                .build()
                .postWithFilesWithToken();
    }

    private void showPasswordDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.password_dialog, null, false);
        dialog.setView(view);
        EditText edit_old_password = view.findViewById(R.id.edit_old_password);
        EditText edit_new_password = view.findViewById(R.id.edit_new_password);
        EditText edit_sure_password = view.findViewById(R.id.edit_sure_password);
        dialog.setTitle("修改密码");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", (dialog1, which) -> {
            String oldPassword = String.valueOf(edit_old_password.getText());
            String newPassword = String.valueOf(edit_new_password.getText());
            String surePassword = String.valueOf(edit_sure_password.getText());
            if (oldPassword == null || oldPassword.equals("") || newPassword == null || newPassword.equals("") || surePassword == null || surePassword.equals("")) {
                Toast.makeText(dialog.getContext(), "输入框不能为空!", Toast.LENGTH_SHORT).show();
                // TODO 输入框会自动消失，暂时不知道如何解决
            } else if (!oldPassword.equals(QiluPreference.getCustomAppProfile("password"))) {
                //原密码不对
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
                dialog2.setTitle("信息错误");
                dialog2.setMessage("原密码不对!");
                dialog2.setCancelable(true);
                dialog2.setPositiveButton("确定", (dialog3, which1) -> dialog3.dismiss());
                dialog2.show();
            } else if (!newPassword.equals(surePassword)) {
                //新密码和确认新密码不对
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
                dialog2.setTitle("信息错误");
                dialog2.setMessage("新密码确认错误!");
                dialog2.setCancelable(true);
                dialog2.setPositiveButton("确定", (dialog3, which1) -> dialog3.dismiss());
                dialog2.show();
            } else {
                Log.i("password", "changed");
                RestClient.builder()
                        .url("http://106.13.96.60:8888/account/update")
                        .loader(getContext())
                        .params("phone", QiluPreference.getCustomAppProfile("phone"))    //TODO 此处获取phone和password的方法不知道会不会产生Bug
                        .params("password", newPassword)
                        .params("name", data.getUserName())
                        .params("signature", "")
                        .success(response -> {
                            Gson gson = new Gson();
                            UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                            int code = userInfo.getCode();
                            if (code == 401) {
                                mISignListener.onTokenExpired();
                            }
                            if (code == 200) {    //TODO 感觉code一般应该一定是200
                                Toast.makeText(getContext(), "更新成功，请重新登录", Toast.LENGTH_SHORT).show();
                                QiluPreference.addCustomAppProfile("password", newPassword);    // TODO 直接修改不知道会不会有Bug
                                userDelegate.onResume();
                                dialog1.dismiss();
                                mISignListener.onTokenExpired();
                            }
                        })
                        .error((code, msg) -> {
                            if (code == 401) {
                                //Token失效
                                mISignListener.onTokenExpired();
                            }
                        })
                        .build()
                        .postRawWithToken();
            }
        });
        dialog.setNegativeButton("取消", (dialog12, which) -> {
            //关闭对话框
            dialog12.dismiss();
        });
        dialog.show();
    }


    private void showNameDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.name_dialog, null, false);
        dialog.setView(view);
        EditText name = view.findViewById(R.id.edit_old_password);
        //对话框标题设置
        dialog.setTitle("修改昵称");
        //对话框设置可以用Back键退出
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", (dialog1, which) -> {
            //关闭对话框
            String newName = String.valueOf(name.getText());
            if (!newName.trim().isEmpty()) {
                // TODO 提交修改昵称
                RestClient.builder()
                        .url("http://106.13.96.60:8888/account/update")
                        .loader(getContext())
                        .params("phone", QiluPreference.getCustomAppProfile("phone"))    //TODO 此处获取phone和password的方法不知道会不会产生Bug
                        .params("password", QiluPreference.getCustomAppProfile("password"))
                        .params("name", newName)
                        .params("signature", "")
                        .success(response -> {
                            Gson gson = new Gson();
                            UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                            int code = userInfo.getCode();
                            if (code == 401) {
                                mISignListener.onTokenExpired();
                            } else if (code == 200) {    //TODO 感觉code一般应该一定是200
                                Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                                userDelegate.onResume();
                                dialog1.dismiss();
                            }
                        })
                        .error((code, msg) -> {
                            if (code == 401) {
                                //Token失效
                                mISignListener.onTokenExpired();
                            }
                        })
                        .build()
                        .postRawWithToken();
            } else {
                Toast.makeText(getContext(), "不能为空", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("取消", (dialog12, which) -> {
            //关闭对话框
            dialog12.dismiss();
        });
        dialog.show();
    }
}
